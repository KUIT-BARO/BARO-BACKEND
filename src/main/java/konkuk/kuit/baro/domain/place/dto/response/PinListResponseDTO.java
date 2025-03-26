package konkuk.kuit.baro.domain.place.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class PinListResponseDTO {

    @Schema(example = "1", description = "핀 Id")
    private Long pinId;

    @Schema(example = "이지환", description = "핀을 작성한 유저의 이름")
    private String username;

    @Schema(example = "스타벅스 건국대점", description = "장소명")
    private String placeName;

    @Schema(example = "서울 광진구 화양동 120-1", description = "주소")
    private String placeAddress;

    @Schema(example = "핀 카테고리", description = "핀에 등록되어있는 카테고리들")
    private List<String> pinCategories = new ArrayList<>();
}
