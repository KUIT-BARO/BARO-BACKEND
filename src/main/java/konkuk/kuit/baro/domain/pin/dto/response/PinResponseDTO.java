package konkuk.kuit.baro.domain.pin.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PinResponseDTO {

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "사용자 이메일", example = "hong@example.com")
    private String userEmail;

    @Schema(description = "사용자 프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String profileImage;

    @Schema(description = "리뷰 내용", example = "아주 좋은 장소였습니다.")
    private String review;

    @Schema(description = "평점", example = "4.5")
    private Short score;

    @Schema(description = "장소 이름", example = "스타벅스 강남점")
    private String placeName;

}
