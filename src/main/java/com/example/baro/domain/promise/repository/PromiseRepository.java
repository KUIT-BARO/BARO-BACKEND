package com.example.baro.domain.promise.repository;

import com.example.baro.domain.promise.entity.Promise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromiseRepository extends JpaRepository<Promise, Long> {

}
