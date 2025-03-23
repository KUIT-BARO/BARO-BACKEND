package konkuk.kuit.baro.domain.promise.repository;

import konkuk.kuit.baro.domain.promise.model.Promise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromiseRepository extends JpaRepository<Promise, Long> {


}
