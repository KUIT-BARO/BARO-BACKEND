package konkuk.kuit.baro.global.auth.entrypoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static konkuk.kuit.baro.global.auth.security.util.AuthErrorResponseUtil.setErrorResponse;

/**
 * [CustomAuthenticationEntryPoint]
 *
 * Spring Security에서 인증(Authentication)에 실패했을 때 호출되는 진입점 클래스입니다.
 *
 * 주요 처리 대상:
 * - Spring Security 내부에서 발생한 AuthenticationException
 *   (ex. UsernameNotFoundException, BadCredentialsException, 인증 객체 없음 등)
 *
 * 동작 방식:
 * - 인증되지 않은 사용자가 보호된 리소스에 접근 시
 * - Spring Security의 ExceptionTranslationFilter가 감지
 * - 이 EntryPoint의 commence() 메서드가 호출됨
 * - 401 Unauthorized 상태 코드와 공통 JSON 에러 응답 반환
 *
 * 처리하지 않는 예외:
 * - 필터 단계에서 발생한 JwtException은 ExceptionHandlerFilter에서 처리됨
 */
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.info("=== AuthenticationEntryPoint 진입 ===");
        setErrorResponse(response, ErrorCode.SECURITY_UNAUTHORIZED);
    }
}
