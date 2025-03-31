package konkuk.kuit.baro.domain.promise.repository;

import konkuk.kuit.baro.domain.promise.model.Promise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromiseRepository extends JpaRepository<Promise, Long> {

    boolean existsById(Long promiseId);

    @Query("SELECT DISTINCT p FROM Promise p " +
            "JOIN FETCH p.promiseMembers pm " +
            "LEFT JOIN FETCH p.place " +
            "WHERE pm.user.id = :userId " +
            "AND (:isHost IS NULL OR pm.isHost = :isHost)")
    List<Promise> findByUserIdAndHost(
            @Param("userId") Long userId,
            @Param("isHost") Boolean isHost);
}
