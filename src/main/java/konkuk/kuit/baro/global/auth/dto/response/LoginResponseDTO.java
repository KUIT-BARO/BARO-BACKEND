package konkuk.kuit.baro.global.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginResponseDTO {

    @Schema(description = "엑세스토큰")
    @NotNull
    private String accessToken;

    @Schema(description = "리프레쉬토큰")
    @NotNull
    private String refreshToken;

    @Schema(description = "유저 아이디", example = "1")
    @NotNull
    private Long userId;

    @Schema(description = "이메일", example = "konkuk@gmail.com")
    @NotNull
    private String email;

    @Schema(description = "이름", example = "이정연")
    @NotNull
    private String name;


}
