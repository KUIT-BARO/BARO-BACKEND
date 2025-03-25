package konkuk.kuit.baro.domain.place.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PlaceSummaryInfoResponseDTO {

    @Schema(example = "서울상상나라", description = "장소명")
    private String placeName;

    @Schema(example = "3.0", description = "별점")
    private Double star;

    @Schema(example = "12", description = "핀 개수")
    private Integer pinCount;

    @Schema(example = "서울 광진구 화양동 5-47", description = "주소")
    private String placeAddress;

    @Schema(example = "[키즈존, 북적이는]", description = "장소 카테고리 이름")
    private List<String> placeCategories;

    public PlaceSummaryInfoResponseDTO(
            String placeName,
            Double star,
            Long pinCount, //COUNT() 가 Long 을 반환
            String placeAddress
    ) {
        this.placeName = placeName;
        this.star = star;
        this.pinCount = pinCount.intValue();
        this.placeAddress = placeAddress;
        this.placeCategories = new ArrayList<>();
    }
}
