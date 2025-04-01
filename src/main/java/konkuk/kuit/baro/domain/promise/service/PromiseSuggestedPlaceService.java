package konkuk.kuit.baro.domain.promise.service;

import konkuk.kuit.baro.domain.place.dto.response.PlaceSearchResponseDTO;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.domain.promise.dto.request.PromisePlaceRequestDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseMemberDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromisePlaceResponseDTO;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.promise.repository.PromiseMemberRepository;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static konkuk.kuit.baro.global.common.response.status.ErrorCode.INVALID_LOCATION;
import static konkuk.kuit.baro.global.common.response.status.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromiseSuggestedPlaceService {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final PromiseMemberRepository promiseMemberRepository;

    public PromisePlaceResponseDTO getSuggestedPlace(PromisePlaceRequestDTO request, Long userId) {
        if (!validateLocation(request.getLatitude(), request.getLongitude())) {
            throw new CustomException(INVALID_LOCATION);
        }

        userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Point suggestedPlaceLocation = GeometryUtil.createPoint(request.getLatitude(), request.getLongitude());

        List<Place> findPlaces = placeRepository.findByLocation(suggestedPlaceLocation);

        List<PlaceSearchResponseDTO> places = new ArrayList<>();
        for (Place place : findPlaces) {
            places.add(new PlaceSearchResponseDTO(place.getId(),
                    place.getLocation().getY(),
                    place.getLocation().getX()));
        }

        List<PromiseMember> promiseMembers = promiseMemberRepository.findAllByPromiseId(request.getPromiseId());
        List<PromiseMemberDTO> promiseMemberDTOS = new ArrayList<>();

        List<PromiseMember> findMembers = promiseMemberRepository.findAllByPromiseId(request.getPromiseId());
        for (PromiseMember member : findMembers) {
            promiseMemberDTOS.add(new PromiseMemberDTO(member.getUser().getId(), member.getUser().getProfileImage()));
        }

        return new PromisePlaceResponseDTO(places, promiseMemberDTOS);


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
