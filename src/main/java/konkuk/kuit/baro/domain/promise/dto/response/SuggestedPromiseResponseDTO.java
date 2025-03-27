package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuggestedPromiseResponseDTO {
    @Schema(description = "약속 ID", example = "1")
    private Long promiseId;

    @Schema(description = "약속명", example = "BARO 회의")
    private String promiseName;

    @Schema(description = "투표까지의 기한", example = "3")
    private int untilVoteDate;

    @Schema(description = "약속 제안된 지역", example = "건대입구 주변")
    private String suggestedRegion;

    @Schema(description = "약속 제안된 시작 날짜", example = "2025-01-01")
    private LocalDate suggestedStartDate;

    @Schema(description = "약속 제안된 끝 날짜", example = "2025-01-02")
    private LocalDate SuggestedEndDate;
}
