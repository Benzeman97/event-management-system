package com.event.api.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Data
public class AttendanceId implements Serializable {

    private String eventId;
    private String userId;
}
