package konkuk.kuit.baro.global.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import konkuk.kuit.baro.global.auth.exception.AuthException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;

    @Getter
    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Getter
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    private static final String BEARER = "Bearer ";

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(Long userId) {
        return Jwts.builder()
                .claim("tokenType", "access")
                .claim("userId", userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationPeriod))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String createRefreshToken(Long userId) {
        return Jwts.builder()
                .claim("tokenType", "refresh")
                .claim("userId", userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationPeriod))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Long getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("userId", Long.class);
    }

    public String getTokenType(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("tokenType", String.class);
    }

    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new AuthException(ErrorCode.SECURITY_INVALID_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new AuthException(ErrorCode.SECURITY_INVALID_TOKEN);
        } catch (MalformedJwtException e) {
            throw new AuthException(ErrorCode.SECURITY_INVALID_TOKEN);
        } catch (SignatureException e) {
            throw new AuthException(ErrorCode.SECURITY_INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new AuthException(ErrorCode.SECURITY_INVALID_TOKEN);
        }
    }

    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (AuthException e) {
            return false;
        }
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.replace(BEARER, ""));
    }
}
