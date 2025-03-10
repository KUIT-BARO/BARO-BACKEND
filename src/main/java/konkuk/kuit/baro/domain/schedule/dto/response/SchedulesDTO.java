package konkuk.kuit.baro.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import konkuk.kuit.baro.domain.schedule.model.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchedulesDTO {
    @Schema(description = "스케줄 ID", example = "1")
    private Long scheduleId;

    @Schema(description = "일정명", example = "수업")
    private String scheduleName;

    @Schema(description = "요일", example = "1")
    private DayOfWeek dayOfWeek;

    @Schema(description = "일정 시작 시간", example = "10:00:00")
    private LocalTime startTime;

    @Schema(description = "일정 종료 시간", example = "12:00:00")
    private LocalTime endTime;
}
