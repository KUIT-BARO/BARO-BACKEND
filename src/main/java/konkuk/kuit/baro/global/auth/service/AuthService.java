package konkuk.kuit.baro.global.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.auth.dto.request.LoginRequestDTO;
import konkuk.kuit.baro.global.auth.dto.response.LoginResponseDTO;
import konkuk.kuit.baro.global.auth.exception.AuthException;
import konkuk.kuit.baro.global.auth.jwt.service.JwtService;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import konkuk.kuit.baro.global.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
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
    public final JwtUtil jwtUtil;

    public LoginResponseDTO login(LoginRequestDTO request) {
        String email = request.getEmail();
        String password = request.getPassword();
        authenticate(email, password);

        String accessToken = jwtUtil.createAccessToken(email);
        String refreshToken = jwtUtil.createRefreshToken();
        jwtService.storeRefreshToken(refreshToken, email);

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    public void reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.extractRefreshToken(request)
                .orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        jwtUtil.isTokenValid(refreshToken);
        jwtService.reissueAndSendTokens(response, refreshToken);
    }

    public void logout(HttpServletRequest request) {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);

        String access = accessToken
                .orElseThrow(() -> new AuthException(ErrorCode.SECURITY_INVALID_ACCESS_TOKEN));
        String refresh = refreshToken
                .orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));

        jwtUtil.isTokenValid(refresh);
        jwtUtil.isTokenValid(access);
        jwtService.deleteRefreshToken(refresh);
        //access token blacklist 처리 -> 로그아웃한 사용자가 요청 시 access token이 redis에 존재하면 jwtAuthenticationFilter에서 인증처리 거부
        jwtService.invalidAccessToken(access);
    }

    public void authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException(ErrorCode.LOGIN_FAILED);
        }
    }
}
