package konkuk.kuit.baro.global.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.baro.global.auth.exception.AuthException;
import konkuk.kuit.baro.global.auth.jwt.service.JwtService;
import konkuk.kuit.baro.global.auth.security.util.CookieUtil;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import konkuk.kuit.baro.global.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Value("${jwt.access.header}")
    private String accessHeader;

    private static final String BEARER = "Bearer ";

    public void reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 Refresh Token 추출
        String refreshToken = cookieUtil.getRefreshTokenCookie(request)
                .orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));

        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new AuthException(ErrorCode.SECURITY_INVALID_REFRESH_TOKEN);
        }

        // Refresh Token에서 userId 추출
        Long userId = jwtUtil.getUserId(refreshToken);

        // Refresh Token 소유권 검증
        jwtService.validateRefreshTokenOwnership(refreshToken, userId);

        // 새로운 토큰 발급
        String newAccessToken = jwtUtil.createAccessToken(userId);
        String newRefreshToken = jwtUtil.createRefreshToken(userId);

        // Redis에 Refresh Token 갱신
        jwtService.rotateRefreshToken(newRefreshToken, userId);

        // Access Token은 헤더로, Refresh Token은 쿠키로 전달
        response.setHeader(accessHeader, BEARER + newAccessToken);
        cookieUtil.addRefreshTokenCookie(response, newRefreshToken, jwtUtil.getRefreshTokenExpirationPeriod());
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = jwtUtil.extractAccessToken(request)
                .orElseThrow(() -> new AuthException(ErrorCode.SECURITY_INVALID_ACCESS_TOKEN));

        if (!jwtUtil.isTokenValid(accessToken)) {
            throw new AuthException(ErrorCode.SECURITY_INVALID_ACCESS_TOKEN);
        }

        Long userId = jwtUtil.getUserId(accessToken);

        // Redis에서 Refresh Token 삭제 + Access Token 블랙리스트 등록
        jwtService.deleteRefreshToken(userId);
        jwtService.invalidAccessToken(accessToken);

        // 쿠키에서 Refresh Token 삭제
        cookieUtil.deleteRefreshTokenCookie(response);
    }
}
