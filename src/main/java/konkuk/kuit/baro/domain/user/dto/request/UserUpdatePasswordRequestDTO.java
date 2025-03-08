package konkuk.kuit.baro.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePasswordRequestDTO{
    @Schema(description = "현재 비밀번호", example = "dlwjddus123")
    @NotBlank
    private String currentPassword;

    @Schema(description = "새 비밀번호", example = "dlwjddus1234")
    @NotBlank
    private String newPassword;

    @Schema(description = "새 비밀번호 확인", example = "dlwjddus1234")
    @NotBlank
    private String confirmPassword;
}
