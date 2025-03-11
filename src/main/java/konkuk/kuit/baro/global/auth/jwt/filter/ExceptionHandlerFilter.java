package konkuk.kuit.baro.global.auth.jwt.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.baro.global.auth.exception.AuthException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import konkuk.kuit.baro.global.common.response.BaseErrorResponse;


import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            setErrorResponse(response, e.getErrorCode());
        } catch (TokenExpiredException e) {
            setErrorResponse(response, ErrorCode.SECURITY_INVALID_TOKEN);
        } catch (AuthenticationException | JWTVerificationException e) {
            setErrorResponse(response, ErrorCode.SECURITY_UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            setErrorResponse(response, ErrorCode.SECURITY_ACCESS_DENIED);
        }
    }

    private void setErrorResponse(
            HttpServletResponse response,
            ErrorCode errorCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // 직렬화 포맷 강제
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        BaseErrorResponse errorResponse = new BaseErrorResponse(errorCode, errorCode.getMessage());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
