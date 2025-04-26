package com.event.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    @CreatedDate
    @Column(name="created_at",updatable = false)
    private Instant createdAt;
    @LastModifiedDate
    @Column(name="updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Event> events;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Attendance> attendances;

}
