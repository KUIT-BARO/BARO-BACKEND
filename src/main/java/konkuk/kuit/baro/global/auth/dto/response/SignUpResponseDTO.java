package konkuk.kuit.baro.global.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignUpResponseDTO {

    @Schema(description = "이메일", example = "konkuk@gmail.com")
    @NotNull
    private String email;

    @Schema(description = "비밀번호", example = "12345")
    @NotNull
    private String password;

    @Schema(description = "이름", example = "이정연")
    @NotNull
    private String name;

}
