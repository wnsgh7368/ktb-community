package ktb.community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    //@PrePersist 발생시 AuditingEntityListener에 전달 -> 현재 시각을 주입해줌
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    //@PreUpdate 발생시 AuditingEntityListener에 전달 -> 현재 시각을 주입해줌
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;
}
