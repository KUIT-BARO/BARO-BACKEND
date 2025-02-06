package com.example.baro.domain.user.repository;

import com.example.baro.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUserId(String userId);
	Optional<User> findByUserId(String userId);

	@Query("""
    SELECT u, 
           CASE WHEN f.id IS NOT NULL THEN true ELSE false END 
    FROM User u
    LEFT JOIN Friend f ON 
        (f.fromUser.id = :userId AND f.toUser.id = u.id) 
        OR (f.fromUser.id = u.id AND f.toUser.id = :userId)
    WHERE u.userId LIKE CONCAT(:code, '%')
""")
	List<Object[]> searchUsersWithFriendStatus(@Param("userId") Long userId, @Param("code") String code);
}
