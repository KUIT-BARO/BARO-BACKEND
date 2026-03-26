package konkuk.kuit.baro.global.auth.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.baro.global.common.response.BaseErrorResponse;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import org.springframework.http.MediaType;

import java.io.IOException;

public class AuthErrorResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    public static void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) {
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
