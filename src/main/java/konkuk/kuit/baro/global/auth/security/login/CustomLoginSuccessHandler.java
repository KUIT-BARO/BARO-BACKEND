package konkuk.kuit.baro.global.auth.security.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.baro.global.auth.dto.response.LoginResponseDTO;
import konkuk.kuit.baro.global.auth.jwt.service.JwtService;
import konkuk.kuit.baro.global.auth.security.util.CookieUtil;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import konkuk.kuit.baro.global.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        String accessToken = jwtUtil.createAccessToken(userId);
        String refreshToken = jwtUtil.createRefreshToken(userId);
        jwtService.storeRefreshToken(refreshToken, userId);

        // Refresh Token은 HttpOnly 쿠키로 전달
        cookieUtil.addRefreshTokenCookie(response, refreshToken, jwtUtil.getRefreshTokenExpirationPeriod());

        // Access Token은 응답 Body로 전달
        LoginResponseDTO loginResponse = new LoginResponseDTO(accessToken);
        BaseResponse<LoginResponseDTO> body = BaseResponse.ok(loginResponse);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(body));

        log.info("로그인 성공: userId={}", userId);
    }
}
