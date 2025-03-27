package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromiseMemberAvailableTimeDTO {
    @Schema(description = "약속 참여자 ID", example = "1")
    private Long promiseMemberId;

    @Schema(description = "날짜", example = "2025/04/04")
    private LocalDate date;

    @Schema(description = "시작 시간", example = "10:00")
    private LocalTime startTime;

    @Schema(description = "종료 시간", example = "12:00")
    private LocalTime endTime;
}
