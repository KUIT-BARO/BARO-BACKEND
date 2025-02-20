package com.example.baro.domain.user.repository;

import com.example.baro.common.entity.Friend;
import com.example.baro.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

	@Query("SELECT f FROM Friend f WHERE f.fromUser.id = :userId")
	List<Friend> findFriendsByUserId(@Param("userId") Long userId);

	@Modifying
	@Query("DELETE FROM Friend f WHERE (f.fromUser.id = :userId1 AND f.toUser.id = :userId2) OR (f.fromUser.id = :userId2 AND f.toUser.id = :userId1)")
	void deleteFriend(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

	@Modifying
	@Transactional
	default void sendFriendRequest(User fromUser, User toUser) {
		save(new Friend(fromUser, toUser));
		save(new Friend(toUser, fromUser));
	}
}