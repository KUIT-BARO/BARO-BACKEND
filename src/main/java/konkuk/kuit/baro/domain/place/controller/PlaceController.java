package konkuk.kuit.baro.domain.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.kuit.baro.domain.place.dto.request.PlaceSearchRequestDTO;
import konkuk.kuit.baro.domain.place.dto.response.PinListResponseDTO;
import konkuk.kuit.baro.domain.place.dto.response.PlaceSearchResponseDTO;
import konkuk.kuit.baro.domain.place.dto.response.PlaceSummaryInfoResponseDTO;
import konkuk.kuit.baro.domain.place.service.PlaceService;
import konkuk.kuit.baro.global.common.annotation.CustomExceptionDescription;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<List<PlaceSearchResponseDTO>> placeSearch(@ModelAttribute @Valid PlaceSearchRequestDTO placeSearchRequestDTO) {
        return BaseResponse.ok(placeService.placeSearch(placeSearchRequestDTO));
    }

    @Tag(name = "장소 탐색 API", description = "장소 탐색 관련 API")
    @Operation(summary = "장소 요약 정보 조회", description = "특정 장소에 대한 요약 정보 ex) 장소명, 별점, 핀 개수 등등을 조회합니다")
    @GetMapping("/{placeId}")
    @CustomExceptionDescription(PLACE_SUMMARY_INFO)
    public BaseResponse<PlaceSummaryInfoResponseDTO> placeSummaryInfo(@PathVariable("placeId") Long placeId) {
        return BaseResponse.ok(placeService.placeSummaryInfo(placeId));
    }

    @Tag(name = "장소 탐색 API", description = "장소 탐색 관련 API")
    @Operation(summary = "장소 핀 목록 조회", description = "특정 장소에 대한 핀 목록을 조회합니다.")
    @GetMapping("/{placeId}/pins")
    @CustomExceptionDescription(PLACE_SUMMARY_INFO)
    public BaseResponse<List<PinListResponseDTO>> placePinList(@PathVariable("placeId") Long placeId) {
        return BaseResponse.ok(placeService.placePinList(placeId));
    }
}
