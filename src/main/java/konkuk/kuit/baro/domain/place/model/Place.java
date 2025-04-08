package konkuk.kuit.baro.domain.place.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.domain.category.model.PlaceCategory;
import konkuk.kuit.baro.domain.pin.model.Pin;
import konkuk.kuit.baro.domain.promise.model.PromiseCandidatePlace;
import konkuk.kuit.baro.domain.promise.model.PromiseSuggestedPlace;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Table(name = "place")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("status IN ('ACTIVE', 'PENDING', 'VOTING', 'CONFIRMED')")
public class Place extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "place_id", nullable = false)
    private Long id;

    @Column(name = "place_name", length = 30, nullable = false)
    private String placeName;

    @Column(name = "location", nullable = false, columnDefinition = "POINT SRID 4326")
    private Point location; // Point (경도, 위도)


    @Column(name = "place_address", length = 50, nullable = false)
    private String placeAddress;

    // 핀을 확인하기 위한 양방향 연관관계
    @OneToMany(mappedBy = "place", orphanRemoval = true)
    private List<Pin> pins = new ArrayList<>();

    // 장소 카테고리를 확인하기 위한 양방향 연관관계
    @OneToMany(mappedBy = "place", orphanRemoval = true)
    private List<PlaceCategory> placeCategories = new ArrayList<>();

    // 장소 삭제시 제안된 약속 장소도 '삭제'하기 위한 양방향 연관 관계
    @OneToMany(mappedBy = "place", orphanRemoval = true)
    private List<PromiseSuggestedPlace> promiseSuggestedPlaces = new ArrayList<>();

    // 장소 삭제시 약속 후보 장소도 '삭제'하기 위한 양방향 연관 관계
    @OneToMany(mappedBy = "place", orphanRemoval = true)
    private List<PromiseCandidatePlace> promiseCandidatePlaces = new ArrayList<>();

    @Builder
    public Place(String placeName, Point location, String placeAddress) {
        this.placeName = placeName;
        this.location = location;
        this.placeAddress = placeAddress;
    }

    // 연관 관계 편의 메서드
    public void addPin(Pin pin) { this.pins.add(pin); }

    public void addPlaceCategory(PlaceCategory placeCategory) { this.placeCategories.add(placeCategory); }

    public void addPromiseSuggestedPlace(PromiseSuggestedPlace promiseSuggestedPlace) {
        this.promiseSuggestedPlaces.add(promiseSuggestedPlace);
    }

    public void addPromiseCandidatePlace(PromiseCandidatePlace promiseCandidatePlace) {
        this.promiseCandidatePlaces.add(promiseCandidatePlace);
    }

    public static Place addPlace(Double latitude, Double longitude, String placeName, String placeAddress){
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        return Place.builder()
                .placeName(placeName)
                .placeAddress(placeAddress)
                .location(location)
                .build();
    }
}
