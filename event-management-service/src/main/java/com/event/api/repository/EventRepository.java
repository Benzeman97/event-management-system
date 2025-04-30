package com.event.api.repository;

import com.event.api.entity.Event;
import com.event.api.enums.EventVisibilityType;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("SELECT e FROM Event e " +
            "WHERE (:startDate IS NULL OR e.startTime >= :startDate) " +
            "AND (:endDate IS NULL OR e.endTime <= :endDate) " +
            "AND (:location IS NULL OR e.location = :location) " +
            "AND (:visibility IS NULL OR e.visibility = :visibility)")
    List<Event> findByFilters(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate,
                                        @Param("location") String location, @Param("visibility") EventVisibilityType visibility);

    @Query("SELECT e FROM Event e WHERE e.startTime > :currentTime ORDER BY e.startTime ASC")
    Page<Event> findUpcomingEvents(@Param("currentTime") Instant currentTime, Pageable pageable);

    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN e.attendances a " +
            "WHERE e.host.id = :userId OR a.user.id = :userId")
    List<Event> findUserEventsByUserId(@Param("userId") UUID userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Event e WHERE e.id = :id")
    Optional<Event> findByIdForUpdate(@Param("id") UUID uuid);
}
