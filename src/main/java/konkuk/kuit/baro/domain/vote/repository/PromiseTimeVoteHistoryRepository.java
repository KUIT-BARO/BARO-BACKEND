package konkuk.kuit.baro.domain.vote.repository;

import konkuk.kuit.baro.domain.promise.model.PromiseCandidateTime;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.vote.model.PromiseTimeVoteHistory;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromiseTimeVoteHistoryRepository extends JpaRepository<PromiseTimeVoteHistory, Long> {

    boolean existsByPromiseVoteId(Long promiseVoteId);

    boolean existsByPromiseVoteAndPromiseMember(PromiseVote promiseVote, PromiseMember promiseMember);

    void deleteAllByPromiseMember(PromiseMember promiseMember);

    // 약속 시간 투표 내역을 확인하여, 가장 많이 투표된 약속 후보 시간별로 내림차순 정렬하여 반환
    @Query("""
            SELECT ptvh.promiseCandidateTime
            FROM PromiseTimeVoteHistory ptvh
            WHERE ptvh.promiseVote.id = :promiseVoteId
            GROUP BY ptvh.promiseCandidateTime
            ORDER BY COUNT(ptvh) DESC
            """)
    List<PromiseCandidateTime> findMostVotedCandidateTime(@Param("promiseVoteId") Long promiseVoteId, Pageable pageable);
}
