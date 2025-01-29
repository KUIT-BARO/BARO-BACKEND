package com.example.baro.domain.user.repository;

import com.example.baro.common.entity.PromisePersonal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromisePersonalRepository extends JpaRepository<PromisePersonal, Long> {

}
