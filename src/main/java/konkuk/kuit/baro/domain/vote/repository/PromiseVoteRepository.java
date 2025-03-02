package konkuk.kuit.baro.domain.vote.repository;

import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromiseVoteRepository extends JpaRepository<PromiseVote, Long> {
}
