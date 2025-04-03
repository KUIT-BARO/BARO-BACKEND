package konkuk.kuit.baro.domain.promise.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.vote.model.PromisePlaceVoteHistory;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "promise_candidate_place")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("status IN ('ACTIVE', 'PENDING', 'VOTING', 'CONFIRMED')")
public class PromiseCandidatePlace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promise_candidate_place_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promis_vote_id", nullable = false)
    private PromiseVote promiseVote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @OneToMany(mappedBy = "promiseCandidatePlace", orphanRemoval = true)
    private List<PromisePlaceVoteHistory> promisePlaceVoteHistories = new ArrayList<>();

    // 생성 메서드
    public static PromiseCandidatePlace createPromiseCandidatePlace(PromiseVote promiseVote, Place place) {
        PromiseCandidatePlace promiseCandidatePlace = new PromiseCandidatePlace();
        promiseCandidatePlace.setPromiseVote(promiseVote);
        promiseCandidatePlace.setPlace(place);
        return promiseCandidatePlace;
    }

    // 연관 관계 편의 메서드
    private void setPromiseVote(PromiseVote promiseVote) {
        this.promiseVote = promiseVote;
        promiseVote.addPromiseCandidatePlace(this);
    }

    private void setPlace(Place place) {
        this.place = place;
        place.addPromiseCandidatePlace(this);
    }

    public void addPromisePlaceVoteHistory(PromisePlaceVoteHistory promisePlaceVoteHistory) {
        this.promisePlaceVoteHistories.add(promisePlaceVoteHistory);
    }
}
