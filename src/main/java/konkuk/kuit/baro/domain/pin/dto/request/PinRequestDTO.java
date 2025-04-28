package konkuk.kuit.baro.domain.pin.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ParameterObject
public class PinRequestDTO {

    @JsonProperty("PlaceId")
    @NotNull(message = "핀 등록 장소는 비어있을 수 없습니다.")
    private long placeId;

    @JsonProperty("review")
    @NotBlank(message = "리뷰 내용은 비어있을 수 없습니다.")
    private String review;

    @JsonProperty("score")
    @NotNull(message = "평점은 비어있을 수 없습니다.")
    private Short score;

    @JsonProperty("categories")
    private List<String> categoryNames;
}

