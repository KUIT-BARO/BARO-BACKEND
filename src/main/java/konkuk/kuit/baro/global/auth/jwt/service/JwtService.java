package konkuk.kuit.baro.global.auth.jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.baro.global.auth.exception.AuthException;
import konkuk.kuit.baro.global.common.util.JwtUtil;
import konkuk.kuit.baro.global.common.redis.RedisService;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String BEARER = "Bearer ";
    private static final String NOT_EXIST = "false";

    private final RedisService redisService;
    private final JwtUtil jwtUtil;

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    public void storeRefreshToken(String refreshToken, String userInfo) {
        redisService.setValues(refreshToken, userInfo,
                Duration.ofMillis(jwtUtil.getRefreshTokenExpirationPeriod()));
    }

    public void deleteRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new AuthException(ErrorCode.SECURITY_UNAUTHORIZED);
        }
        redisService.delete(refreshToken);
    }

    public void invalidAccessToken(String accessToken) {
        redisService.setValues(accessToken, "logout",
                Duration.ofMillis(jwtUtil.getAccessTokenExpirationPeriod()));
    }

    public String findRefreshTokenAndExtractUserInfo(String refreshToken) {
        String userInfo = redisService.getValues(refreshToken);

        if (userInfo.equals(NOT_EXIST)) {
            throw new AuthException(ErrorCode.SECURITY_INVALID_REFRESH_TOKEN);
        }
        return userInfo;
    }

    public List<String> reissueAndSendTokens(HttpServletResponse response, String refreshToken) {
        // 기존 Refresh Token 검증 및 사용자 정보 추출
        String userInfo = findRefreshTokenAndExtractUserInfo(refreshToken);

        // 기존 Refresh Token 폐기 (DB나 Redis에서 삭제)
        deleteRefreshToken(refreshToken);

        // 새로운 Refresh Token 발급
        String reissuedRefreshToken = jwtUtil.createRefreshToken();
        String reissuedAccessToken = jwtUtil.createAccessToken(userInfo);

        // 새로운 Refresh Token을 DB나 Redis에 저장
        storeRefreshToken(reissuedRefreshToken, userInfo);

        List<String> tokenList = new ArrayList<>();
        tokenList.add(reissuedRefreshToken);
        tokenList.add(reissuedAccessToken);

        return tokenList;
    }
}