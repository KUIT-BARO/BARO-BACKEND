package konkuk.kuit.baro.domain.pin.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PinPageResponseDTO {
    List<CategoryDTO> categoryDTOs;
}
