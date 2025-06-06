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
public class PromiseMemberDTO{
    @Schema(description = "유저 ID", example = "1")
    private Long userId;

    @Schema(description = "유저 프로필 사진", example = "image")
    private String profileImage;
}