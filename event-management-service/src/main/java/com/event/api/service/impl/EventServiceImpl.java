package com.event.api.service.impl;

import com.event.api.dto.request.CreateEventRequest;
import com.event.api.entity.Event;
import com.event.api.enums.EventVisibilityType;
import com.event.api.exception.ApplicationException;
import com.event.api.repository.EventRepository;
import com.event.api.service.EventService;
import com.event.api.util.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventServiceImpl implements EventService {

    final private static Logger LOGGER = LogManager.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository){
        this.eventRepository=eventRepository;
    }

    public String createEvent(CreateEventRequest request){

        try {

            LocalDateTime startTime = DateTimeUtil.parseToLocalDateTime(request.getStartTime());
            LocalDateTime endTime = DateTimeUtil.parseToLocalDateTime(request.getEndTime());

            Event event = buildEvent(request.getTitle(), request.getDescription(), request.getLocation(), startTime, endTime,
                    EventVisibilityType.valueOf(request.getEventVisibilityType().toUpperCase()));

            event = eventRepository.save(event);

            LOGGER.info("New event has been created with event id {}", event.getId());

            return event.getId().toString();
        } catch (Exception ex){
            LOGGER.error("An error occurred while creating event: {}", ex.getMessage(), ex);
            throw new ApplicationException(100001,"error.create.event");
        }
    }

    private Event buildEvent(String title, String description, String location,
                              LocalDateTime startTime, LocalDateTime endTime, EventVisibilityType eventVisibilityType) {

        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setLocation(location);
        event.setStartTime(DateTimeUtil.convertLocalDateTimeToInstant(startTime));
        event.setEndTime(DateTimeUtil.convertLocalDateTimeToInstant(endTime));
        event.setVisibility(eventVisibilityType);
        return event;
    }

}
