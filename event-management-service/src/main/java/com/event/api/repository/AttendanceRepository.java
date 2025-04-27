package com.event.api.repository;

import com.event.api.entity.Attendance;
import com.event.api.entity.AttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {

    Optional<Attendance> findByEventIdAndUserId(UUID eventId, UUID userId);
}
