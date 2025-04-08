package konkuk.kuit.baro.domain.place.service;

import konkuk.kuit.baro.domain.category.repository.PlaceCategoryRepository;
import konkuk.kuit.baro.domain.pin.model.Pin;
import konkuk.kuit.baro.domain.pin.repository.PinRepository;
import konkuk.kuit.baro.domain.place.dto.request.PlaceSearchRequestDTO;
import konkuk.kuit.baro.domain.place.dto.response.PinListResponseDTO;
import konkuk.kuit.baro.domain.place.dto.response.PlaceSearchResponseDTO;
import konkuk.kuit.baro.domain.place.dto.response.PlaceSummaryInfoResponseDTO;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static konkuk.kuit.baro.global.common.response.status.ErrorCode.INVALID_LOCATION;
import static konkuk.kuit.baro.global.common.response.status.ErrorCode.PLACE_NOT_FOUND;
import static konkuk.kuit.baro.global.common.util.GeometryUtil.createPoint;
import static konkuk.kuit.baro.global.common.util.GeometryUtil.validateLocation;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceCategoryRepository placeCategoryRepository;
    private final PinRepository pinRepository;

    public List<PlaceSearchResponseDTO> placeSearch(PlaceSearchRequestDTO request) {

        if (!validateLocation(request.getLatitude(), request.getLongitude())) {
            throw new CustomException(INVALID_LOCATION);
        }

        Point currentUserLocation = createPoint(request.getLatitude(), request.getLongitude());

        List<Place> places = placeRepository.findByDistanceAndPlaceCategories(currentUserLocation, request.getPlaceCategoryIds());

        return places.stream()
                .map(place -> new PlaceSearchResponseDTO(place.getId(), place.getLocation().getY(), place.getLocation().getX()))
                .toList();
    }


    public PlaceSummaryInfoResponseDTO placeSummaryInfo(Long placeId) {

        PlaceSummaryInfoResponseDTO placeSummary = placeRepository.findPlaceSummaryById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        List<String> placeCategories = placeCategoryRepository.findCategoryNamesByPlaceId(placeId);

        placeSummary.setPlaceCategories(placeCategories);

        return placeSummary;
    }

    public List<PinListResponseDTO> placePinList(Long placeId) {

        Place findPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(PLACE_NOT_FOUND));

        List<Pin> pins = findPlace.getPins();

        return pins.stream()
                .map(pin -> new PinListResponseDTO(pin.getId(),
                        extractUsernameFromPin(pin),
                        findPlace.getPlaceName(),
                        findPlace.getPlaceAddress(),
                        pin.getCategoryNameList()))
                .toList();
    }

    private String extractUsernameFromPin(Pin pin) {
        return pin.getUser().getName();
    }

}
