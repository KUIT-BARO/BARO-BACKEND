package com.example.baro.common.dto;

import com.example.baro.common.dto.enums.ErrorCode;
import com.example.baro.common.dto.enums.SuccessCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micrometer.common.lang.Nullable;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
public record ApiResponseDto<T>(
		@JsonIgnore HttpStatus httpStatus, // JSON 출력에서 제외
		int status,                       // HTTP 상태 코드
		int code,                         // 비즈니스 로직 상태 코드
		@NonNull String message,          // 메시지
		@JsonInclude(value = NON_NULL) T data // 선택적 데이터 (null이면 출력되지 않음)
) {
	// 성공 응답 (데이터 포함)
	public static <T> ApiResponseDto<T> success(final SuccessCode successCode, @Nullable final T data) {
		return ApiResponseDto.<T>builder()
				.httpStatus(successCode.getHttpStatus())
				.status(successCode.getHttpStatus().value())
				.code(successCode.getCode())
				.message(successCode.getMessage())
				.data(data)
				.build();
	}

	// 성공 응답 (데이터 없음)
	public static <T> ApiResponseDto<T> success(final SuccessCode successCode) {
		return ApiResponseDto.<T>builder()
				.httpStatus(successCode.getHttpStatus())
				.status(successCode.getHttpStatus().value())
				.code(successCode.getCode())
				.message(successCode.getMessage())
				.data(null)
				.build();
	}

	// 실패 응답
	public static <T> ApiResponseDto<T> fail(final ErrorCode errorCode) {
		return ApiResponseDto.<T>builder()
				.httpStatus(errorCode.getHttpStatus())
				.status(errorCode.getHttpStatus().value())
				.code(errorCode.getCode())
				.message(errorCode.getMessage())
				.data(null)
				.build();
	}
}