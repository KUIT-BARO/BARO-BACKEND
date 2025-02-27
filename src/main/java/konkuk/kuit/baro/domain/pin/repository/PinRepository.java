package konkuk.kuit.baro.domain.pin.repository;

import konkuk.kuit.baro.domain.pin.model.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinRepository extends JpaRepository<Pin, Long> {
}
