package konkuk.kuit.baro.domain.promise.repository;

import konkuk.kuit.baro.domain.promise.dto.response.PromiseMemberAvailableTimeDTO;
import konkuk.kuit.baro.domain.promise.model.PromiseAvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromiseAvailableTimeRepository extends JpaRepository<PromiseAvailableTime, Long> {

    @Query("SELECT new konkuk.kuit.baro.domain.promise.dto.response." +
           "PromiseMemberAvailableTimeDTO(pa.promiseMember.id, pa.availableDate, pa.availableStartTime, pa.availableEndTime) " +
           "FROM PromiseAvailableTime pa " +
           "WHERE pa.promiseMember.id = :promiseMemberId ")
    List<PromiseMemberAvailableTimeDTO> findAllByPromiseMemberId(Long promiseMemberId);
}
