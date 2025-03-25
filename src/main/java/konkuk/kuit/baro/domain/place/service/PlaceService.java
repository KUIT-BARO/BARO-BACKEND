package konkuk.kuit.baro.domain.place.service;

import konkuk.kuit.baro.domain.category.repository.PlaceCategoryRepository;
import konkuk.kuit.baro.domain.pin.model.Pin;
import konkuk.kuit.baro.domain.place.dto.request.PlaceSearchRequestDTO;
import konkuk.kuit.baro.domain.place.dto.response.PlaceSearchResponseDTO;
import konkuk.kuit.baro.domain.place.dto.response.PlaceSummaryInfoResponseDTO;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import konkuk.kuit.baro.global.common.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static konkuk.kuit.baro.global.common.response.status.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceCategoryRepository placeCategoryRepository;

    public List<PlaceSearchResponseDTO> placeSearch(PlaceSearchRequestDTO request) {

        if (!validateLocation(request.getLatitude(), request.getLongitude())) {
            throw new CustomException(INVALID_LOCATION);
        }

        Point currentUserLocation = GeometryUtil.createPoint(request.getLatitude(), request.getLongitude());

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

    private Boolean validateLocation(Double latitude, Double longitude) {
        if (latitude > 90 || latitude < -90) {
            return false;
        }

        if (longitude > 180 || longitude < -180) {
            return false;
        }

        return true;
    }
}
