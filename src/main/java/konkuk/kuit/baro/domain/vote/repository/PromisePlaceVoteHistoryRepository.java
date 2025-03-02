package konkuk.kuit.baro.domain.vote.repository;

import konkuk.kuit.baro.domain.vote.model.PromisePlaceVoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromisePlaceVoteHistoryRepository extends JpaRepository<PromisePlaceVoteHistory, Long> {
}
