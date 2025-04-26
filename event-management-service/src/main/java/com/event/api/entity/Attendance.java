package com.event.api.entity;

import com.event.api.enums.AttendanceStatusType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name="Attendance")
@NoArgsConstructor
@Data
public class Attendance {

    @Column(name = "event_id")
    private String eventId;
    
    @Column(name = "user_id")
    private String userId;
    
    @Enumerated(EnumType.STRING)
    private AttendanceStatusType status;
    @Column(name="responded_at")
    private Instant respondedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
