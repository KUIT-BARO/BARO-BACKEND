package konkuk.kuit.baro.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileSettingResponseDTO {
    @Schema(description = "이름", example = "이정연")
    private String userName;

    @Schema(description = "이메일", example = "dlwjddus1112@naver.com")
    private String email;

    @Schema(description = "프로필 사진", example = "image")
    private String profileImage;
}
