package com.example.baro.domain.user.entity;

import com.example.baro.domain.user.util.status.UserStatus;
import com.example.baro.domain.user.util.status.UserStatusConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = UserStatusConverter.class)
    private UserStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "user_id", nullable = false, length = 20, unique = true)
    private String userId;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(name = "profile_image", columnDefinition = "TEXT")
    private String profileImage;

    @Builder
    public User(String userId, String password, String nickname, String profileImage){
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = UserStatus.ACTIVE;
        }
    }
}
