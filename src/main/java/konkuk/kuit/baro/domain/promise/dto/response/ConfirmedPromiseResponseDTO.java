package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmedPromiseResponseDTO {
    @Schema(description = "약속 ID", example = "1")
    private Long promiseId;

    @Schema(description = "약속명", example = "BARO 회의")
    private String promiseName;

    @Schema(description = "확정 인원 이름", example = "김상균, 이정연, 신종윤")
    private List<String> promiseMembersNames;

    @Schema(description = "약속 확정된 장소명", example = "건대입구역")
    private String placeName;

    @Schema(description = "약속 확정된 날짜", example = "2025-01-01")
    private LocalDate fixedDate;
}
