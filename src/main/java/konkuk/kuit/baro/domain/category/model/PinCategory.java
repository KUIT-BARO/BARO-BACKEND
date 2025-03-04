package konkuk.kuit.baro.domain.category.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.domain.pin.model.Pin;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "pin_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("status IN ('ACTIVE', 'PENDING', 'VOTING', 'CONFIRMED')")
public class PinCategory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pin_category_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id", nullable = false)
    private Pin pin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // 생성 메서드
    public static PinCategory createPinCategory(Pin pin, Category category) {
        PinCategory pinCategory = new PinCategory();
        pinCategory.setPin(pin);
        pinCategory.setCategory(category);

        return pinCategory;
    }

    // 연관 관계 편의 메서드
    public void setPin(Pin pin) {
        this.pin = pin;
        pin.addPinCategory(this);
    }

    public void setCategory(Category category) {
        this.category = category;
        category.addPinCategory(this);
    }




}
