package konkuk.kuit.baro.domain.pin.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.domain.category.model.PinCategory;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pin")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("status IN ('ACTIVE', 'BEFORE_VOTE', 'DURING_VOTE', 'AFTER_VOTE')")
public class Pin extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pin_id", nullable = false)
    private Long id;

    @Lob
    @Column(name = "review", nullable = false)
    private String review;

    @Column(name = "score", nullable = false)
    private Short score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    // 핀 카테고리를 확인하기 위한 양방향 연관 관계
    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PinCategory> pinCategories = new ArrayList<>();

    // 생성 메서드
    public static Pin createPin(String review, Short score, User user, Place place) {
        Pin pin = new Pin();
        pin.review = review;
        pin.score = score;
        pin.setUser(user);
        pin.setPlace(place);

        return pin;
    }

    // 연관 관계 편의 메서드
    public void setUser(User user) {
        this.user = user;
        user.addPin(this);
    }

    public void setPlace(Place place) {
        this.place = place;
        place.addPin(this);
    }

    public void addPinCategory(PinCategory pinCategory) { this.pinCategories.add(pinCategory); }
}
