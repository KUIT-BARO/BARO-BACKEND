package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuggestedPromiseResponseDTO {
    @Schema(description = "약속 ID", example = "1")
    private Long promiseId;

    @Schema(description = "약속명", example = "BARO 회의")
    private String promiseName;

    @Schema(description = "투표까지의 기한", example = "3")
    private int untilDate;

    @Schema(description = "약속 제안 지역", example = "BARO 회의")
    private String suggestedRegion;

    @Schema(description = "약속 시작 날짜", example = "2025-01-01")
    private LocalDate startDate;

    @Schema(description = "약속 끝 시간", example = "2025-01-02")
    private LocalDate endDate;
}
