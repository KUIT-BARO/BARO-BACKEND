package com.example.baro.domain.promise.repository;

import com.example.baro.common.entity.PromiseVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromiseVoteRepository extends JpaRepository<PromiseVote, Long> {
    List<PromiseVote> findByPromiseId(Long promiseId);
}
