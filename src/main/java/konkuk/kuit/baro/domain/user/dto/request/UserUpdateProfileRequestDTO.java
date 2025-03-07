package konkuk.kuit.baro.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateProfileRequestDTO {
    @Schema(description = "이름", example = "이정연")
    @NotNull
    private String newName;

    @Schema(description = "프로필 사진",example = "image1")
    @NotNull
    private String newProfileImage;

}
