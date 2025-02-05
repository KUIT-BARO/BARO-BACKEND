package com.example.baro.domain.promise.repository;

import com.example.baro.common.entity.PromisePersonalTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromisePersonalTimeRepository extends JpaRepository<PromisePersonalTime, Long> {
    List<PromisePersonalTime> findByPromisePersonalIdIn(List<Long> personalPromiseIds);
}
