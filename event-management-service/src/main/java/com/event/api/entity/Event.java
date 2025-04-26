package com.event.api.entity;

import com.event.api.enums.EventVisibilityType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name="event")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class Event {

    @Id
    private String id;
    private String title;
    private String description;
    private Instant startTime;
    private Instant endTime;
    private String location;
    @Enumerated(EnumType.STRING)
    private EventVisibilityType visibility;
    @CreatedDate
    @Column(name="created_at")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name="updated_at")
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", insertable = false, updatable = false)
    private User host; 
}
