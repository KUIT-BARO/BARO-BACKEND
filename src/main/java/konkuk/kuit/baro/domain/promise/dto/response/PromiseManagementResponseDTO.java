package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class PromiseManagementResponseDTO {
    @Schema(description = "제안된 약속들")
    private List<SuggestedPromiseResponseDTO> suggestedPromises;

    @Schema(description = "투표중인 약속들")
    private List<VotingPromiseResponseDTO> votingPromises;

    @Schema(description = "확정된 약속들")
    private List<ConfirmedPromiseResponseDTO> confirmedPromises;

}
