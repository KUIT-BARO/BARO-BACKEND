package konkuk.kuit.baro.domain.vote.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "promise_vote")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("status IN ('ACTIVE', 'PENDING', 'VOTING', 'CONFIRMED')")
public class PromiseVote extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promise_vote_id", nullable = false)
    private Long id;

    @Column(name = "vote_end_time", nullable = false)
    private LocalDateTime voteEndTime;

    // 투표된 시간들을 확인하기 위한 양방향 연관 관계
    @OneToMany(mappedBy = "promiseVote", orphanRemoval = true)
    private List<PromiseTimeVoteHistory> promiseTimeVoteHistories = new ArrayList<>();

    // 투표된 장소들을 확인하기 위한 양방향 연관 관계
    @OneToMany(mappedBy = "promiseVote", orphanRemoval = true)
    private List<PromisePlaceVoteHistory> promisePlaceVoteHistories = new ArrayList<>();

    @Builder
    public PromiseVote(LocalDateTime voteEndTime) {
        this.voteEndTime = voteEndTime;
    }

    // 연관 관계 편의 메서드
    public void addPromiseTimeVoteHistory(PromiseTimeVoteHistory promiseTimeVoteHistory) {
        this.promiseTimeVoteHistories.add(promiseTimeVoteHistory);
    }

    public void addPromisePlaceVoteHistory(PromisePlaceVoteHistory promisePlaceVoteHistory) {
        this.promisePlaceVoteHistories.add(promisePlaceVoteHistory);
    }
}
