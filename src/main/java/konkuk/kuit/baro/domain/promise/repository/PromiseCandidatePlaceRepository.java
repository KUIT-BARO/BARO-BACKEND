package konkuk.kuit.baro.domain.promise.repository;

import konkuk.kuit.baro.domain.promise.model.PromiseCandidatePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromiseCandidatePlaceRepository extends JpaRepository<PromiseCandidatePlace, Long> {
}
