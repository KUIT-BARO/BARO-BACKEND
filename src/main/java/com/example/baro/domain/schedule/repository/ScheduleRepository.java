package com.example.baro.domain.schedule.repository;

import com.example.baro.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<List<Schedule>> findByUserId(Long userId);
}
