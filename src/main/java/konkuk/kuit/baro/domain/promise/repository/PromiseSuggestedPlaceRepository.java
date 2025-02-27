package konkuk.kuit.baro.domain.promise.repository;

import konkuk.kuit.baro.domain.promise.model.PromiseSuggestedPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromiseSuggestedPlaceRepository extends JpaRepository<PromiseSuggestedPlace, Long> {
}
