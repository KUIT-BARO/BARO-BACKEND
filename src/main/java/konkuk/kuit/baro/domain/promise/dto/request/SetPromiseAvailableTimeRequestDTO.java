package konkuk.kuit.baro.domain.promise.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetPromiseAvailableTimeRequestDTO {
    @NotNull
    @Size(min = 1, message = "최소 하나 이상의 시간을 입력해야 합니다.")
    List<TimeDTO> times;
}
