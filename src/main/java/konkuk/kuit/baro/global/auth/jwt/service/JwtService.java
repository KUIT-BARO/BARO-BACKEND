package konkuk.kuit.baro.global.auth.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.redis.RedisService;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Getter
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USER_INFO_CLAIM = "userInfo";
    private static final String BEARER = "Bearer ";
    private static final String NOT_EXIST = "false";

    private final UserRepository userRepository;
    private final RedisService redisService;

    public String createAccessToken(String email) {
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
                .withClaim(USER_INFO_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String reissueRefreshToken(String email) {
        String reissuedRefreshToken = createRefreshToken();
        updateRefreshToken(reissuedRefreshToken, email);
        return reissuedRefreshToken;
    }

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

    //RefreshToken redis 저장
    public void updateRefreshToken(String refreshToken, String userInfo) {
        redisService.setValues(refreshToken, userInfo,
                Duration.ofMillis(refreshTokenExpirationPeriod));
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public void deleteRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new CustomException(ErrorCode.SECURITY_UNAUTHORIZED);
        }
        redisService.delete(refreshToken);
    }

    public void invalidAccessToken(String accessToken) {
        redisService.setValues(accessToken, "logout",
                Duration.ofMillis(accessTokenExpirationPeriod));
    }

    public String findRefreshTokenAndExtractUserInfo(String refreshToken) {
        String userInfo = redisService.getValues(refreshToken);

        if (userInfo.equals(NOT_EXIST)) {
            throw new CustomException(ErrorCode.SECURITY_INVALID_REFRESH_TOKEN);
        }
        return userInfo;
    }

    /*private void sendTokens(HttpServletResponse response, String reissuedAccessToken,
                            String reissuedRefreshToken) {
        response.setHeader(accessHeader, BEARER + reissuedAccessToken);
        response.setHeader(refreshHeader, BEARER + reissuedRefreshToken);
    }*/

    public List<String> reissueAndSendTokens(HttpServletResponse response, String refreshToken) {
        String userInfo = findRefreshTokenAndExtractUserInfo(refreshToken);
        String reissuedRefreshToken = reissueRefreshToken(userInfo);
        String reissuedAccessToken = createAccessToken(userInfo);

        List<String> tokenList = new ArrayList<>();
        tokenList.add(reissuedRefreshToken);
        tokenList.add(reissuedAccessToken);

        return tokenList;
    }

    public String extractUserInfo(String token) {
        return JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(token)
                .getClaim(USER_INFO_CLAIM)
                .asString();
    }

}