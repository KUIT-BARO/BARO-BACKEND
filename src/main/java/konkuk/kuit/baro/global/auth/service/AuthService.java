package konkuk.kuit.baro.global.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.auth.dto.request.LoginRequestDTO;
import konkuk.kuit.baro.global.auth.dto.request.SignUpRequestDTO;
import konkuk.kuit.baro.global.auth.dto.response.LoginResponseDTO;
import konkuk.kuit.baro.global.auth.dto.response.ReissueResponseDTO;
import konkuk.kuit.baro.global.auth.exception.AuthException;
import konkuk.kuit.baro.global.auth.jwt.service.JwtService;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    public final JwtService jwtService;
    public final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO request) {
        String email = request.getEmail();
        String password = request.getPassword();
        authenticate(email, password);

        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.storeRefreshToken(refreshToken, email);

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    public ReissueResponseDTO reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.extractRefreshToken(request)
                .orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        jwtService.isTokenValid(refreshToken);

        List<String> tokenList = jwtService.reissueAndSendTokens(response, refreshToken);
        String accessTokenResponse = tokenList.get(0);
        String refreshTokenResponse = tokenList.get(1);

        return new ReissueResponseDTO(accessTokenResponse, refreshTokenResponse );
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
        //access token blacklist 처리 -> 로그아웃한 사용자가 요청 시 access token이 redis에 존재하면 jwtAuthenticationFilter에서 인증처리 거부
        jwtService.invalidAccessToken(access);
    }

    public void authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND)); // 바로 예외 발생

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
