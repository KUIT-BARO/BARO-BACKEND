package com.example.baro.domain.user.repository;

import com.example.baro.common.entity.User;
import com.example.baro.common.entity.UserPromise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPromiseRepository extends JpaRepository<UserPromise, Long> {
	List<UserPromise> findAllByUserAndDisplayTrue(User user);
}
