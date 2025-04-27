package com.event.api.service.impl;

import com.event.api.entity.Attendance;
import com.event.api.exception.DataNotFoundException;
import com.event.api.repository.AttendanceRepository;
import com.event.api.service.AttendanceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    final private static Logger LOGGER = LogManager.getLogger(AttendanceServiceImpl.class);

    private final AttendanceRepository attendanceRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository){
        this.attendanceRepository=attendanceRepository;
    }

    @Override
    public String getAttendanceStatusForEvent(UUID eventId, UUID userId) {

        Attendance attendance = attendanceRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(()->{
                    LOGGER.error("Attendance not found for Event ID {} and User ID {}", eventId, userId);
                    throw new DataNotFoundException("error.data.not.found");
                });
        LOGGER.info("Fetching attendance status for Event ID {} and User ID {}", eventId, userId);
        return attendance.getStatus().toString();
    }
}
