package com.event.api.service;

import com.event.api.dto.request.UpdateEventRequest;
import com.event.api.dto.request.CreateEventRequest;


public interface EventService {

    String createEvent(CreateEventRequest request);
    String updateEvent(UpdateEventRequest request);

}
