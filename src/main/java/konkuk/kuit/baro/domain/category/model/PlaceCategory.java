package konkuk.kuit.baro.domain.category.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.domain.pin.model.Pin;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "place_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("status IN ('ACTIVE', 'BEFORE_VOTE', 'DURING_VOTE', 'AFTER_VOTE')")
public class PlaceCategory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_category_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // 생성 메서드
    public static PlaceCategory createPlaceCategory(Place place, Category category) {
        PlaceCategory placeCategory = new PlaceCategory();
        placeCategory.setPlace(place);
        placeCategory.setCategory(category);

        return placeCategory;
    }

    // 연관 관계 편의 메서드
    public void setPlace(Place place) {
        this.place = place;
        place.addPlaceCategory(this);
    }

    public void setCategory(Category category) {
        this.category = category;
        category.addPlaceCategory(this);
    }
}
