package konkuk.kuit.baro.domain.promise.repository;

import konkuk.kuit.baro.domain.promise.dto.request.TimeDTO;
import konkuk.kuit.baro.domain.promise.model.PromiseAvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromiseAvailableTimeRepository extends JpaRepository<PromiseAvailableTime, Long> {

    @Query("SELECT new konkuk.kuit.baro.domain.promise.dto.request." +
            "TimeDTO(pa.availableDate, pa.availableStartTime, pa.availableEndTime) " +
            "FROM PromiseAvailableTime pa " +
            "WHERE pa.promiseMember.id = :promiseMemberId ")
    List<TimeDTO> findAllByPromiseMemberId(Long promiseMemberId);

    // 특정 약속에 대해 약속 가능 시간 표시 내역이 존재하는 지 확인
    @Query("SELECT CASE WHEN EXISTS (" +
            "SELECT 1 FROM PromiseAvailableTime pat " +
            "WHERE pat.promiseMember.promise.id = :promiseId) " +
            "THEN TRUE ELSE FALSE END")
    boolean existsPromiseAvailableTimeByPromiseId(@Param("promiseId") Long promiseId);

    // 특정 약속 참여자가 약속 가능 시간 표시를 했는지 확인
    @Query("SELECT CASE WHEN EXISTS (" +
            "SELECT 1 FROM PromiseAvailableTime pat " +
            "WHERE pat.promiseMember.id = :promiseMemberId) " +
            "THEN TRUE ELSE FALSE END")
    boolean existsPromiseAvailableTimeByPromiseMemberId(@Param("promiseMemberId") Long promiseMemberId);

}
