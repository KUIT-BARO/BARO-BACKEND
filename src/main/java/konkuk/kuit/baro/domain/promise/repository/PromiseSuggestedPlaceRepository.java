package konkuk.kuit.baro.domain.promise.repository;

import konkuk.kuit.baro.domain.promise.model.PromiseSuggestedPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromiseSuggestedPlaceRepository extends JpaRepository<PromiseSuggestedPlace, Long> {

    // 특정 약속에 대해 약속 장소 제안 내역이 존재하는 지 확인
    @Query("SELECT CASE WHEN EXISTS (" +
            "SELECT 1 FROM PromiseSuggestedPlace psp " +
            "WHERE psp.promiseMember.promise.id = :promiseId) " +
            "THEN TRUE ELSE FALSE END")
    boolean existsPromiseSuggestedPlaceByPromiseId(@Param("promiseId") Long promiseId);

    // 특정 약속 참여자가 약속 장소 제안을 했는지 확인
    @Query("SELECT CASE WHEN EXISTS (" +
            "SELECT 1 FROM PromiseSuggestedPlace psp " +
            "WHERE psp.promiseMember.id = :promiseMemberId) " +
            "THEN TRUE ELSE FALSE END")
    boolean existsPromiseSuggestedPlaceByPromiseMemberId(@Param("promiseMemberId") Long promiseMemberId);

    @Query("SELECT CASE WHEN EXISTS (" +
           "SELECT 1 FROM PromiseSuggestedPlace psp " +
           "WHERE psp.promiseMember.promise.id = :promiseId " +
           "AND psp.place.id = :placeId) " +
           "THEN TRUE ELSE FALSE END")
    boolean existsByPlaceIdAndPromiseId(@Param("placeId") Long placeId, @Param("promiseId") Long promiseId);

}
