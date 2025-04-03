package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateTimesDTO {

    @Schema(description = "약속 후보 시간 ID", example = "1")
    private Long promiseCandidateTimeId;

    @Schema(description = "약속 후보 시간 - 날짜 정보", example = "2025년 1월 2일")
    private String date;

    @Schema(description = "약속 후보 시간 - 시간 정보", example = "14:00")
    private String time;

    @Schema(description = "기존에 해당 약속 후보 시간에 대해 투표했는지 여부", example = "true")
    private Boolean isSelected;
}
