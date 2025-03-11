package konkuk.kuit.baro.domain.promise.repository;

import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromiseMemberRepository extends JpaRepository<PromiseMember, Long> {

    @Query("SELECT pm.color FROM PromiseMember pm WHERE pm.id = :promiseId")
    List<String> findColorsByPromiseId(Long promiseId);
}
