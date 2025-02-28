package konkuk.kuit.baro.global.common.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.global.common.response.status.BaseStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BaseStatus status = BaseStatus.ACTIVE;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
