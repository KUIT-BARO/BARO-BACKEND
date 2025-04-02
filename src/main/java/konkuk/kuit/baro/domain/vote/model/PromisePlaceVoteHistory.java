package konkuk.kuit.baro.domain.vote.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.promise.model.PromiseSuggestedPlace;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "promise_place_vote_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("status IN ('ACTIVE', 'PENDING', 'VOTING', 'CONFIRMED')")
public class PromisePlaceVoteHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promise_place_vote_history_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promise_suggested_place_id", nullable = false)
    private PromiseSuggestedPlace promiseSuggestedPlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promise_vote_id", nullable = false)
    private PromiseVote promiseVote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promise_member_id", nullable = false)
    private PromiseMember promiseMember;

    // 생성 메서드
    public static PromisePlaceVoteHistory createPromisePlaceVoteHistory(PromiseSuggestedPlace promiseSuggestedPlace, PromiseVote promiseVote, PromiseMember promiseMember) {
        PromisePlaceVoteHistory promisePlaceVoteHistory = new PromisePlaceVoteHistory();
        promisePlaceVoteHistory.setPromiseSuggestedPlace(promiseSuggestedPlace);
        promisePlaceVoteHistory.setPromiseVote(promiseVote);
        promisePlaceVoteHistory.setPromiseMember(promiseMember);
        return promisePlaceVoteHistory;
    }

    // 연관 관계 편의 메서드
    private void setPromiseSuggestedPlace(PromiseSuggestedPlace promiseSuggestedPlace) {
        this.promiseSuggestedPlace = promiseSuggestedPlace;
        promiseSuggestedPlace.addPromisePlaceVoteHistory(this);
    }

    private void setPromiseVote(PromiseVote promiseVote) {
        this.promiseVote = promiseVote;
        promiseVote.addPromisePlaceVoteHistory(this);
    }

    private void setPromiseMember(PromiseMember promiseMember) {
        this.promiseMember = promiseMember;
        promiseMember.addPromisePlaceVoteHistory(this);
    }
}
