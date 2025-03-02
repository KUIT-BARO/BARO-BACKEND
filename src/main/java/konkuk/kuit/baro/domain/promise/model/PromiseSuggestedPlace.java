package konkuk.kuit.baro.domain.promise.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.vote.model.PromisePlaceVoteHistory;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "promise_suggested_place")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("status IN ('ACTIVE', 'BEFORE_VOTE', 'DURING_VOTE', 'AFTER_VOTE')")
public class PromiseSuggestedPlace extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promise_suggested_place_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promise_member_id", nullable = false)
    private PromiseMember promiseMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    // 제안된 약속 장소 삭제시, 약속 장소 투표 내역도 사라지도록 하기 위한 양방향 연관 관계
    @OneToMany(mappedBy = "promiseSuggestedPlace", orphanRemoval = true)
    private List<PromisePlaceVoteHistory> promisePlaceVoteHistories = new ArrayList<>();


    // 생성 메서드
    public static PromiseSuggestedPlace createPromiseSuggestedPlace(PromiseMember promiseMember, Place place) {
        PromiseSuggestedPlace promiseSuggestedPlace = new PromiseSuggestedPlace();
        promiseSuggestedPlace.setPromiseMember(promiseMember);
        promiseSuggestedPlace.setPlace(place);

        return promiseSuggestedPlace;
    }

    // 연관 관계 편의 메서드
    private void setPromiseMember(PromiseMember promiseMember) {
        this.promiseMember = promiseMember;
        promiseMember.addPromiseSuggestedPlace(this);
    }

    private void setPlace(Place place) {
        this.place = place;
        place.addPromiseSuggestedPlace(this);
    }

    public void addPromisePlaceVoteHistory(PromisePlaceVoteHistory promisePlaceVoteHistory) {
        this.promisePlaceVoteHistories.add(promisePlaceVoteHistory);
    }
}
