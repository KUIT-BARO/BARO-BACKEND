package konkuk.kuit.baro.global.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CodeCheckRequestDTO {
    @Schema(description = "인증 번호(인증 코드)", example = "85mNvlC5")
    @NotNull
    private String authCode;
}
