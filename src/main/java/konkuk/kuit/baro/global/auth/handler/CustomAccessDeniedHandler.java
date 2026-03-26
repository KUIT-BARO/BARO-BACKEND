package konkuk.kuit.baro.global.auth.handler;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.kuit.baro.global.common.response.BaseErrorResponse;

import java.io.IOException;

import static konkuk.kuit.baro.global.auth.security.util.AuthErrorResponseUtil.setErrorResponse;


public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {

        setErrorResponse(response, ErrorCode.SECURITY_ACCESS_DENIED);
    }
}
