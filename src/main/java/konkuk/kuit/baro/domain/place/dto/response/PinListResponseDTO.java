package konkuk.kuit.baro.domain.place.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class PinListResponseDTO {

    @Schema(example = "1", description = "핀 Id")
    private Long pinId;

    @Schema(example = "스타벅스 건국대점", description = "장소명")
    private String placeName;

    @Schema(example = "서울 광진구 화양동 120-1", description = "주소")
    private String placeAddress;
}
