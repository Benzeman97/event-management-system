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

    @EmbeddedId
    private AttendanceId id;
    @Enumerated(EnumType.STRING)
    private AttendanceStatusType status;
    @Column(name="responded_at")
    private Instant respondedAt;

}
