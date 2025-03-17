package konkuk.kuit.baro.domain.promise.repository;

import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromiseMemberRepository extends JpaRepository<PromiseMember, Long> {
    List<PromiseMember> findAllByUserId(Long userId);
    
    @Query("SELECT COUNT(*) FROM PromiseMember pm where pm.promise.id = :promiseId")
    int findNumberOfPromiseMemberById(Long promiseId);

    @Query(value = "SELECT u.name " +
                   "FROM promise_member pm " +
                   "JOIN users u ON pm.user_id = u.user_id " +
                   "WHERE pm.promise_id = :promiseId AND pm.is_host = 1", nativeQuery = true)
    String findHostNameByPromiseId(@Param("promiseId") Long promiseId);


    @Query("SELECT pm.color FROM PromiseMember pm WHERE pm.id = :promiseId")
    List<String> findColorsByPromiseId(Long promiseId);
}
