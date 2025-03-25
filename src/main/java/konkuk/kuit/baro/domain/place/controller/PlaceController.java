package konkuk.kuit.baro.domain.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.kuit.baro.domain.place.dto.response.PlacesResponseDTO;
import konkuk.kuit.baro.domain.place.service.PlaceService;
import konkuk.kuit.baro.global.common.annotation.CustomExceptionDescription;
import konkuk.kuit.baro.global.common.config.swagger.SwaggerResponseDescription;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static konkuk.kuit.baro.global.common.config.swagger.SwaggerResponseDescription.*;

@Slf4j
@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @Tag(name = "장소 탐색 API", description = "장소 탐색 관련 API")
    @Operation(summary = "장소 탐색", description = "현재 좌표와 카테고리를 기준으로 반경 2KM 이내의 장소를 조회합니다.")
    @GetMapping
    @CustomExceptionDescription(PLACE_SEARCH)
    public BaseResponse<List<PlacesResponseDTO>> placeSearch(@RequestParam("placeCategoryIds") List<Long> placeCategoryIds,
                                                             @RequestParam("latitude") Double latitude,
                                                             @RequestParam("longitude") Double longitude
    ) {
        return BaseResponse.ok(placeService.placeSearch(placeCategoryIds, latitude, longitude));
    }
}
