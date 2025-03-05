package konkuk.kuit.baro.domain.category.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;
import java.util.ArrayList;


@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("status IN ('ACTIVE', 'PENDING', 'VOTING', 'CONFIRMED')")
public class Category extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long id;

    @Column(name = "category_name", length = 10, nullable = false)
    private String categoryName;

    // 카테고리 삭제시 핀 카테고리를 '삭제' 하기 위한 양방향 연관 관계
    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private List<PinCategory> pinCategories = new ArrayList<>();

    // 카테고리 삭제시 장소 카테고리를 '삭제' 하기 위한 양방향 연관 관계
    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private List<PlaceCategory> placeCategories = new ArrayList<>();

    @Builder
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    // 연관 관계 편의 메서드
    public void addPinCategory(PinCategory pinCategory) { this.pinCategories.add(pinCategory); }

    public void addPlaceCategory(PlaceCategory placeCategory) { this.placeCategories.add(placeCategory); }
}
