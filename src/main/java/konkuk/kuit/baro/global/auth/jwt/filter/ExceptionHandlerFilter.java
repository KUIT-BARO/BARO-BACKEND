package konkuk.kuit.baro.global.auth.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.baro.global.auth.jwt.exception.CustomJwtException;
import konkuk.kuit.baro.global.auth.security.exception.CustomAuthenticationException;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static konkuk.kuit.baro.global.auth.security.util.AuthErrorResponseUtil.setErrorResponse;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("=== JwtExceptionHandlerFilter 진입 ===");

        try {
            filterChain.doFilter(request, response);
        } catch (CustomJwtException e) {
            SecurityContextHolder.clearContext();
            setErrorResponse(response, e.getErrorCode());
        } catch (CustomException e) {
            SecurityContextHolder.clearContext();
            setErrorResponse(response, e.getErrorCode());
        }
    }
}
