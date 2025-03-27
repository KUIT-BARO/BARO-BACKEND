package konkuk.kuit.baro.global.auth.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SignUpRequestDTO{

    @Schema(description = "이메일", example = "konkuk@gmail.com")
    @NotNull
    String email;

    @Schema(description = "비밀번호", example = "12345")
    @NotNull
    String password;

    @Schema(description = "이름", example = "이정연")
    @NotNull
    String name;
}
