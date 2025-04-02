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
public class PromiseMemberSuggestStateDTO {

    @Schema(description = "제안을 얼만큼 수행했는지 여부", example = "COMPLETE")
    private String suggestionProgress;

    @Schema(description = "약속 제안자 여부", example = "true")
    private Boolean isHost;

    @Schema(description = "프로필 이미지", example = "DOG")
    private String profileImage;
}
