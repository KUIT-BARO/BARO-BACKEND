package com.example.baro.domain.user.repository;

import com.example.baro.common.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
	@Query("SELECT f FROM Friend f WHERE f.isFriend = true AND (f.fromUser.id = :userId OR f.toUser.id = :userId)")
	List<Friend> findFriendsByUserId(@Param("userId") Long userId);
}
