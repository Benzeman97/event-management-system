package com.event.api.service;

import com.event.api.dto.request.CreateEventRequest;
import com.event.api.enums.EventVisibilityType;

import java.time.LocalDateTime;

public interface EventService {

    String createEvent(CreateEventRequest request);

}
