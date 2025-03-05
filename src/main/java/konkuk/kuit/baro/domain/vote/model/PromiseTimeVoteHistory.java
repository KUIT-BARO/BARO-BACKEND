package konkuk.kuit.baro.domain.vote.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.domain.promise.model.PromiseAvailableTime;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "promise_time_vote_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("status IN ('ACTIVE', 'PENDING', 'VOTING', 'CONFIRMED')")
public class PromiseTimeVoteHistory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promise_time_vote_history", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promise_available_time_id", nullable = false)
    private PromiseAvailableTime promiseAvailableTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promise_vote_id", nullable = false)
    private PromiseVote promiseVote;

    // 생성 메서드
    public static PromiseTimeVoteHistory createPromiseTimeVoteHistory(PromiseAvailableTime promiseAvailableTime, PromiseVote promiseVote) {
        PromiseTimeVoteHistory promiseTimeVoteHistory = new PromiseTimeVoteHistory();
        promiseTimeVoteHistory.setPromiseAvailableTime(promiseAvailableTime);
        promiseTimeVoteHistory.setPromiseVote(promiseVote);

        return promiseTimeVoteHistory;
    }

    // 연관 관계 편의 메서드
    private void setPromiseAvailableTime(PromiseAvailableTime promiseAvailableTime) {
        this.promiseAvailableTime = promiseAvailableTime;
        promiseAvailableTime.addPromiseTimeVoteHistory(this);
    }

    private void setPromiseVote(PromiseVote promiseVote) {
        this.promiseVote = promiseVote;
        promiseVote.addPromiseTimeVoteHistory(this);
    }

}
