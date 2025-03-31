package konkuk.kuit.baro.domain.promise.repository;

import konkuk.kuit.baro.domain.promise.dto.response.PromiseMemberDTO;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromiseMemberRepository extends JpaRepository<PromiseMember, Long> {
    List<PromiseMember> findAllByUserId(Long userId);

    List<PromiseMember> findAllByPromiseId(Long promiseId);

    @Query("SELECT COUNT(*) FROM PromiseMember pm where pm.promise.id = :promiseId")
    int findNumberOfPromiseMemberById(Long promiseId);

    @Query(value = "SELECT u.name " +
                   "FROM promise_member pm " +
                   "JOIN users u ON pm.user_id = u.user_id " +
                   "WHERE pm.promise_id = :promiseId AND pm.is_host = 1", nativeQuery = true)
    String findHostNameByPromiseId(@Param("promiseId") Long promiseId);


    @Query("SELECT pm.color FROM PromiseMember pm WHERE pm.id = :promiseId")
    List<String> findColorsByPromiseId(Long promiseId);

    @Query("SELECT DISTINCT p FROM Promise p " +
            "JOIN FETCH p.promiseMembers pm " +
            "LEFT JOIN FETCH p.place " +
            "WHERE pm.user.id = :userId " +
            "AND (:isHost IS NULL OR pm.isHost = :isHost)")
    List<Promise> findByUserIdAndHost(
            @Param("userId") Long userId,
            @Param("isHost") Boolean isHost);

    @Query("SELECT pm.user.name FROM PromiseMember pm WHERE pm.promise.id = :promiseId")
    List<String> findMemberNamesByPromiseId(@Param("promiseId") Long promiseId);

    @Query("SELECT new konkuk.kuit.baro.domain.promise.dto.response." +
           "PromiseMemberDTO(pm.user.id,pm.user.profileImage) " +
           "FROM PromiseMember pm WHERE pm.promise.id = :promiseId")
    List<PromiseMemberDTO> findPromiseMemberDTOByPromiseId(Long promiseId);

    @Query("SELECT pm.user.id " +
           "FROM PromiseMember pm " +
           "WHERE pm.promise.id = :promiseId")
    List<Long> findUserIdListByPromiseid(Long promiseId);

    PromiseMember findByUserIdAndPromiseId(Long userId, Long promiseId);
}
