package com.event.api.service;

import com.event.api.dto.request.UpdateEventRequest;
import com.event.api.dto.request.CreateEventRequest;
import com.event.api.dto.response.EventDetailsResponse;
import com.event.api.entity.Event;
import com.event.api.model.EventFilterCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


public interface EventService {

    String createEvent(CreateEventRequest request);
    String updateEvent(UpdateEventRequest request);
    void deleteEvent(UUID eventId);
    List<Event> getFilteredEvents(EventFilterCriteria filterCriteria);
    Page<Event> getUpcomingEvents(Pageable pageable);
    List<Event> getUserEvents(UUID userId);
    EventDetailsResponse getEventDetails(UUID eventId);
}
