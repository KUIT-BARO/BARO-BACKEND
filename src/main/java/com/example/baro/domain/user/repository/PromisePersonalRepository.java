package com.example.baro.domain.user.repository;

import com.example.baro.common.Enum.status.Status;
import com.example.baro.common.entity.PromisePersonal;
import com.example.baro.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromisePersonalRepository extends JpaRepository<PromisePersonal, Long> {
	List<PromisePersonal> findAllByUser(User user);

	List<PromisePersonal> findAllByUserAndStatus(User user, Status status);

	List<PromisePersonal> findAllByPromiseId(Long promiseId);

	@Query("SELECT p.id FROM PromisePersonal p WHERE p.promise.id = :promiseId AND p.status = :status")
	List<Long> findActivePersonalPromiseIdsByPromiseId(@Param("promiseId") Long promiseId, @Param("status") Status status);


}
