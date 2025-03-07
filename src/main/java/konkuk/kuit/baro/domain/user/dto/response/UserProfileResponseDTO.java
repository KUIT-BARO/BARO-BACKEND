package konkuk.kuit.baro.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserProfileResponseDTO {
    @Schema(description = "이름", example = "이정연")
    private String name;

    @Schema(description = "프로필 사진", example = "image")
    private String profileImage;
}
