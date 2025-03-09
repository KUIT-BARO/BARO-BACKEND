package konkuk.kuit.baro.domain.promise.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PromiseSuggestRequestDTO {

    @Schema(description = "약속 이름", example = "KUIT BARO 2차 회의")
    @NotBlank(message = "약속 이름은 비어있을 수 없습니다.")
    private String promiseName;

    @Schema(description = "약속 제안 시작일", example = "2025-03-09")
    @NotNull(message = "약속 제안 시작일은 비어있을 수 없습니다.")
    private LocalDate suggestedStartDate;

    @Schema(description = "약속 제안 종료일", example = "2025-03-13")
    @NotNull(message = "약속 제안 종료일은 비어있을 수 없습니다.")
    private LocalDate suggestedEndDate;

    @Schema(description = "초기 약속 제안 장소", example = "건국대학교 근처")
    @NotBlank(message = "제안된 지역은 비어있을 수 없습니다.")
    private String suggestedRegion;
}
