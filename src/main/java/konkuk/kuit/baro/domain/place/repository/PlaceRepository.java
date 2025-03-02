package konkuk.kuit.baro.domain.place.repository;

import konkuk.kuit.baro.domain.place.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
}
