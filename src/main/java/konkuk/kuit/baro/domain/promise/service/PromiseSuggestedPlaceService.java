package konkuk.kuit.baro.domain.promise.service;

import konkuk.kuit.baro.domain.place.dto.response.PlaceSearchResponseDTO;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.domain.promise.dto.request.SetPromiseSuggestedPlaceRequestDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseMemberDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromisePlaceResponseDTO;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.promise.model.PromiseSuggestedPlace;
import konkuk.kuit.baro.domain.promise.repository.PromiseMemberRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseSuggestedPlaceRepository;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static konkuk.kuit.baro.global.common.response.status.ErrorCode.*;
import static konkuk.kuit.baro.global.common.util.GeometryUtil.validateLocation;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PromiseSuggestedPlaceService {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final PromiseMemberRepository promiseMemberRepository;
    private final PromiseRepository promiseRepository;
    private final PromiseSuggestedPlaceRepository promiseSuggestedPlaceRepository;
    private final PromiseAvailableTimeService promiseAvailableTimeService;

    public PromisePlaceResponseDTO getSuggestedPlace(Double latitude, Double longitude, Long promiseId) {
        if (!validateLocation(latitude, longitude)) {
            throw new CustomException(INVALID_LOCATION);
        }

        Point suggestedPlaceLocation = GeometryUtil.createPoint(latitude, longitude);

        List<Place> findPlaces = placeRepository.findByLocation(suggestedPlaceLocation);

        List<PlaceSearchResponseDTO> places = findPlaces.stream()
                .map(place -> new PlaceSearchResponseDTO(place.getId(), place.getLocation().getY(), place.getLocation().getX()))
                .toList();

        List<PromiseMemberDTO> promiseMemberDTOs = promiseMemberRepository.findPromiseMemberDTOByPromiseId(promiseId);

        Promise promise = promiseRepository.findById(promiseId).orElseThrow(
                () -> new CustomException(PROMISE_NOT_FOUND)
        );

        return new PromisePlaceResponseDTO(promise.getSuggestedRegion(), places, promiseMemberDTOs);
    }


    public List<PlaceSearchResponseDTO> getCategorySearchPlaces(List<String> categories, Double latitude, Double longitude) {
        if (!validateLocation(latitude, longitude)) {
            throw new CustomException(INVALID_LOCATION);
        }

        Point suggestedPlaceLocation = GeometryUtil.createPoint(latitude, longitude);
        List<Place> findPlaces = placeRepository.findByCategoriesAndDistance(categories, suggestedPlaceLocation);

        return findPlaces.stream()
                .map(place -> new PlaceSearchResponseDTO(place.getId(), place.getLocation().getY(), place.getLocation().getX()))
                .toList();
    }

    @Transactional
    public void setPromiseSuggestedPlace(SetPromiseSuggestedPlaceRequestDTO req, Long userId){

        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Promise promise = promiseRepository.findById(req.getPromiseId()).orElseThrow(() -> new CustomException(PROMISE_NOT_FOUND));
        if(promiseSuggestedPlaceRepository.existsByPlaceIdAndPromiseId(req.getPlaceId(), req.getPromiseId())) {
            throw new CustomException(PLACE_ALREADY_CHOSEN);
        }
        promiseAvailableTimeService.addPromiseMember(req.getPromiseId(), loginUser, promise);

        PromiseMember promiseMember = promiseMemberRepository.findByUserIdAndPromiseId(userId, req.getPromiseId());

        if(req.getPlaceId() == -1){
            Place newPlace = Place.addPlace(req.getLatitude(), req.getLongitude(), req.getPlaceName(), req.getAddress());
            placeRepository.save(newPlace);
            PromiseSuggestedPlace suggestedPlace = PromiseSuggestedPlace.createPromiseSuggestedPlace(promiseMember, newPlace);
            promiseSuggestedPlaceRepository.save(suggestedPlace);
            return;
        }

        Place findPlace = placeRepository.findById(req.getPlaceId()).orElseThrow(() -> new CustomException(PLACE_NOT_FOUND));
        PromiseSuggestedPlace suggestedPlace = PromiseSuggestedPlace.createPromiseSuggestedPlace(promiseMember, findPlace);
        promiseSuggestedPlaceRepository.save(suggestedPlace);
    }

}
