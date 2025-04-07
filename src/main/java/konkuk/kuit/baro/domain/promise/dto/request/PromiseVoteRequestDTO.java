package konkuk.kuit.baro.domain.promise.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PromiseVoteRequestDTO {

    @NotEmpty(message = "최소 하나 이상의 약속 후보 시간에 투표해야합니다.")
    @Schema(description = "투표한 약속 후보 시간들의 식별 ID", example = "[1, 2, 3]")
    private List<Long> promiseCandidateTimeIds;

    @NotEmpty(message = "최소 하나 이상의 약속 후보 장소에 투표해야합니다.")
    @Schema(description = "투표한 약속 후보 장소들의 식별 ID", example = "[1, 2, 3]")
    private List<Long> promiseCandidatePlaceIds;
}
