package konkuk.kuit.baro.global.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MailRequestDTO {

    @Schema(description = "이메일", example = "konkuk@gmail.com")
    @NotNull
    private String email;
}
