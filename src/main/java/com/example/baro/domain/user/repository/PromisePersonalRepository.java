package com.example.baro.domain.user.repository;

import com.example.baro.common.entity.PromisePersonal;
import com.example.baro.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromisePersonalRepository extends JpaRepository<PromisePersonal, Long> {
	List<PromisePersonal> findAllByUser(User user);
}
