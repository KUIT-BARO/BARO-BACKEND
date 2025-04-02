package konkuk.kuit.baro.domain.vote.repository;

import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.vote.model.PromiseTimeVoteHistory;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromiseTimeVoteHistoryRepository extends JpaRepository<PromiseTimeVoteHistory, Long> {

    boolean existsByPromiseVoteId(Long promiseVoteId);

    boolean existsByPromiseVoteAndPromiseMember(PromiseVote promiseVote, PromiseMember promiseMember);
}
