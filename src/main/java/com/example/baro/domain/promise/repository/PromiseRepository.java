package com.example.baro.domain.promise.repository;

import com.example.baro.common.entity.Promise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromiseRepository extends JpaRepository<Promise, Long> {

}
