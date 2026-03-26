package konkuk.kuit.baro.global.auth.security.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {

    private final String cookieName;

    public CookieUtil(@Value("${jwt.refresh.cookie-name}") String cookieName) {
        this.cookieName = cookieName;
    }

    // 로그인 및 재발급 시
    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, long maxAgeMillis) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(maxAgeMillis / 1000)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public Optional<String> getRefreshTokenCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    // 로그아웃 시
    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
