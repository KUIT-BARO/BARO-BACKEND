package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PendingPromiseResponseDTO {

    @Schema(description = "약속 이름", example = "KUIT BARO 2차 회의")
    private String promiseName;

    @Schema(description = "투표를 시작할 준비가 되었는지 여부", example = "true")
    private Boolean isVotingReady;

    @Schema(description = "약속에 참여한 유저들의 정보")
    private List<PromiseMemberSuggestStateDTO> promiseMemberSuggestStates;

}
