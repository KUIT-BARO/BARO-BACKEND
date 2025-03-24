package konkuk.kuit.baro.domain.place.service;

import konkuk.kuit.baro.domain.place.dto.response.PlacesResponseDTO;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;

    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public List<PlacesResponseDTO> placeSearch(Double latitude, Double longitude) {
        Point currentUserLocation = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        List<Place> places = placeRepository.findByDistance(currentUserLocation);

        return places.stream()
                .map(place -> new PlacesResponseDTO(place.getId(), place.getLocation().getY(), place.getLocation().getX()))
                .toList();
    }
}
