package com.example.baro.common.exception.dto;

import com.example.baro.common.dto.enums.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponseDto {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int statusCode;
    private final String statusCodeName;
    private final String code;
    private final String message;
    private final String runtimeValue;

    public static ResponseEntity<ErrorResponseDto> toResponseEntity(
            ErrorCode errorCode, String runtimeValue
    ) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponseDto.builder()
                        .statusCode(errorCode.getHttpStatus().value())
                        .statusCodeName(errorCode.getHttpStatus().name())
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .runtimeValue(runtimeValue)
                        .build()
                );
    }
}