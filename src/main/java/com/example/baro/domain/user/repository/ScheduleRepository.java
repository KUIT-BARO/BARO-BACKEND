package com.example.baro.domain.user.repository;

import com.example.baro.common.entity.Schedule;
import com.example.baro.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
	List<Schedule> findAllByUser(User user);
}
