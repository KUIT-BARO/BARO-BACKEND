package konkuk.kuit.baro.global.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.domain.user.service.UserService;
import konkuk.kuit.baro.global.auth.dto.request.LoginRequestDTO;
import konkuk.kuit.baro.global.auth.dto.response.LoginResponseDTO;
import konkuk.kuit.baro.global.auth.exception.AuthException;
import konkuk.kuit.baro.global.auth.jwt.service.JwtService;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.StringTokenizer;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    public final JwtService jwtService;
    public final UserRepository userRepository;
    public final UserService userService;

    public LoginResponseDTO login(LoginRequestDTO request) {
        String email = request.getEmail();
        String password = request.getPassword();

        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(refreshToken, email);
        Optional<User> userOptional = userRepository.findByEmailAndPassword(email, password);
        if (userOptional.isEmpty()) {
            throw new AuthException(ErrorCode.USER_NOT_FOUND);
        }

        User user = userOptional.get();
        return new LoginResponseDTO(accessToken, refreshToken, user.getId(), email, user.getName());
    }

    public void reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.extractRefreshToken(request)
                .orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        jwtService.isTokenValid(refreshToken);

        jwtService.reissueAndSendTokens(response, refreshToken);
    }

    public void logout(Optional<String> accessToken, Optional<String> refreshToken) {
        String access = accessToken
                .orElseThrow(() -> new AuthException(ErrorCode.SECURITY_INVALID_ACCESS_TOKEN));
        String refresh = refreshToken
                .orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));

        jwtService.isTokenValid(refresh);
        jwtService.isTokenValid(access);
        //refresh token 삭제
        jwtService.deleteRefreshToken(refresh);
        //access token blacklist 처리 -> 로그아웃한 사용자가 요청 시 access token이 redis에 존재하면 jwtAuthenticationProcessingFilter에서 인증처리 거부
        jwtService.invalidAccessToken(access);
    }

}
