package com.example.baro.domain.schedule.repository;

import com.example.baro.common.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
