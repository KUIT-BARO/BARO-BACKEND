package konkuk.kuit.baro.domain.schedule.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddScheduleRequestDTO {
    @Schema(description = "일정명", example = "수업")
    private String scheduleName;

    @Schema(description = "요일", example = "1(월요일)")
    private Integer dayOfWeek;

    @Schema(description = "시작 시간", example = "11:00:00")
    private LocalTime startTime;

    @Schema(description = "종료 시간", example = "12:00:00")
    private LocalTime endTime;

    @Schema(description = "장소", example = "건국대학교")
    private String placeName;

}
