package konkuk.kuit.baro.domain.promise.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.domain.vote.model.PromiseTimeVoteHistory;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "promise_candidate_time")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("status IN ('ACTIVE', 'PENDING', 'VOTING', 'CONFIRMED')")
public class PromiseCandidateTime extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promise_candidate_time_id", nullable = false)
    private Long id;

    @Column(name = "promise_candidate_time_date", nullable = false, columnDefinition = "DATE")
    private LocalDate promiseCandidateTimeDate;

    @Column(name = "promise_candidate_time_start_time", nullable = false)
    private LocalTime promiseCandidateTimeStartTime;

    @Column(name = "promise_candidate_time_end_time", nullable = false)
    private LocalTime promiseCandidateTimeEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promis_vote_id", nullable = false)
    private PromiseVote promiseVote;

    @OneToMany(mappedBy = "promiseCandidateTime", orphanRemoval = true)
    private List<PromiseTimeVoteHistory> promiseTimeVoteHistories = new ArrayList<>();


    // 생성 메서드
    public static PromiseCandidateTime createPromiseCandidateTime(LocalDate promiseCandidateTimeDate, LocalTime promiseCandidateTimeStartTime, LocalTime promiseCandidateTimeEndTime, PromiseVote promiseVote) {
        PromiseCandidateTime promiseCandidateTime = new PromiseCandidateTime();
        promiseCandidateTime.promiseCandidateTimeDate = promiseCandidateTimeDate;
        promiseCandidateTime.promiseCandidateTimeStartTime = promiseCandidateTimeStartTime;
        promiseCandidateTime.promiseCandidateTimeEndTime = promiseCandidateTimeEndTime;
        promiseCandidateTime.setPromiseVote(promiseVote);
        return promiseCandidateTime;
    }

    // 연관 관계 편의 메서드
    private void setPromiseVote(PromiseVote promiseVote) {
        this.promiseVote = promiseVote;
        promiseVote.addPromiseCandidateTime(this);
    }

    public void addPromiseTimeVoteHistory(PromiseTimeVoteHistory promiseTimeVoteHistory) {
        this.promiseTimeVoteHistories.add(promiseTimeVoteHistory);
    }




}
