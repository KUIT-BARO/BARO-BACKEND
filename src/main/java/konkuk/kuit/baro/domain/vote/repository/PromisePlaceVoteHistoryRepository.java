package konkuk.kuit.baro.domain.vote.repository;

import konkuk.kuit.baro.domain.promise.model.PromiseCandidatePlace;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.vote.model.PromisePlaceVoteHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromisePlaceVoteHistoryRepository extends JpaRepository<PromisePlaceVoteHistory, Long> {

    void deleteAllByPromiseMember(PromiseMember promiseMember);

    // 약속 장소 투표 내역을 확인하여, 가장 많이 투표된 약속 후보 시간별로 내림차순 정렬하여 반환
    @Query("""
            SELECT ppvh.promiseCandidatePlace
            FROM PromisePlaceVoteHistory ppvh
            WHERE ppvh.promiseVote.id = :promiseVoteId
            GROUP BY ppvh.promiseCandidatePlace
            ORDER BY COUNT(ppvh) DESC
            """)
    List<PromiseCandidatePlace> findMostVotedCandidatePlace(@Param("promiseVoteId") Long promiseVoteId, Pageable pageable);
}
