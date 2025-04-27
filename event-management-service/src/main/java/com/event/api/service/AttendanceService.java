package com.event.api.service;

import java.util.UUID;

public interface AttendanceService {

    String getAttendanceStatusForEvent(UUID eventId, UUID userId);
}
