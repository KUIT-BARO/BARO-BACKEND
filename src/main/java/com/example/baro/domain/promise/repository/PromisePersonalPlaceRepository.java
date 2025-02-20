package com.example.baro.domain.promise.repository;

import com.example.baro.common.entity.PromisePersonalPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromisePersonalPlaceRepository extends JpaRepository<PromisePersonalPlace, Long> {
    PromisePersonalPlace findByPromisePersonalId(Long promisePersonalId);

    List<PromisePersonalPlace> findByPromisePersonal_IdIn(List<Long> personalPromiseIds);
}
