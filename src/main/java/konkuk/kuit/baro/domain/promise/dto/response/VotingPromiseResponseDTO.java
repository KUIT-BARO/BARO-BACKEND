package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VotingPromiseResponseDTO {

    @Schema(description = "약속 이름", example = "KUIT BARO 2차 회의")
    private String promiseName;

    @Schema(description = "투표를 종료할 수 있는지 여부", example = "true")
    private Boolean canCloseVoting;

    @Schema(description = "약속에 참여한 유저들의 투표 여부 관련 정보")
    private List<PromiseMemberVoteStateDTO> promiseMemberVoteStateDTO;


}
