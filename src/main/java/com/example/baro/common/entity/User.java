package com.example.baro.common.entity;

import com.example.baro.common.Enum.userProfile.UserProfile;
import com.example.baro.common.Enum.userProfile.UserProfileConverter;
import com.example.baro.common.Enum.status.Status;
import com.example.baro.common.Enum.status.StatusConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = StatusConverter.class)
    private Status status;

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

    @Column(nullable = false)
    @Convert(converter = UserProfileConverter.class)
    private UserProfile profileImage;

    @Builder
    public User(String userId, String password, String nickname, int profileImage){
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
    }

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = Status.ACTIVE;
        }
        if (this.profileImage == null) {
            this.profileImage = UserProfile.NONE;
        }
    }
}
