package com.event.api.service.impl;

import com.event.api.dto.request.CreateEventRequest;
import com.event.api.dto.request.UpdateEventRequest;
import com.event.api.entity.Event;
import com.event.api.enums.EventVisibilityType;
import com.event.api.exception.ApplicationException;
import com.event.api.exception.DataNotFoundException;
import com.event.api.repository.EventRepository;
import com.event.api.service.EventService;
import com.event.api.service.UserService;
import com.event.api.util.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {

    final private static Logger LOGGER = LogManager.getLogger(EventServiceImpl.class);

    private final UserService userService;
    private final EventRepository eventRepository;

    public EventServiceImpl(UserService userService,EventRepository eventRepository){
        this.userService=userService;
        this.eventRepository=eventRepository;
    }

    @Override
    public String createEvent(CreateEventRequest request){

        try {
            Event event = buildEvent(request);
            event = eventRepository.save(event);
            LOGGER.info("New event has been created with event id {}", event.getId());
            return event.getId().toString();
        }catch (DateTimeParseException ex) {
            LOGGER.error("Invalid date format for event creation", ex);
            throw new ApplicationException(100003, "error.invalid.date.format");
        } catch (Exception ex){
            LOGGER.error("An error occurred while creating event",ex);
            throw new ApplicationException(100001,"error.create.event");
        }
    }

    @Override
    public String updateEvent(UpdateEventRequest request) {
        try{

              Event event = eventRepository.findById(UUID.fromString(request.getEventId()))
                      .orElseThrow(()->{
                          LOGGER.error("Event with ID {} not found", request.getEventId());
                          throw new DataNotFoundException("error.data.not.found");
                      });
               event = updateEvent(event,request);
               eventRepository.save(event);
               LOGGER.info("Event updated successfully with id {}", request.getEventId());
                return event.getId().toString();
        } catch (DateTimeParseException ex) {
            LOGGER.error("Invalid date format for event with ID {}", request.getEventId());
            throw new ApplicationException(100003, "error.invalid.date.format");
        } catch (Exception ex){
            LOGGER.error("An error occurred while updating event",ex);
            throw new ApplicationException(100002, "error.update.event");
        }

    }

    private Event buildEvent(CreateEventRequest request) {

        LocalDateTime startTime = DateTimeUtil.parseToLocalDateTime(request.getStartTime());
        LocalDateTime endTime = DateTimeUtil.parseToLocalDateTime(request.getEndTime());

        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setLocation(request.getLocation());
        event.setStartTime(DateTimeUtil.convertLocalDateTimeToInstant(startTime));
        event.setEndTime(DateTimeUtil.convertLocalDateTimeToInstant(endTime));
        event.setVisibility(EventVisibilityType.valueOf(request.getEventVisibilityType().toUpperCase()));
        event.setHost(userService.getHost(request.getHostId()));
        return event;
    }

    private Event updateEvent(Event event,UpdateEventRequest request) {

            LocalDateTime startTime = DateTimeUtil.parseToLocalDateTime(request.getStartTime());
            LocalDateTime endTime = DateTimeUtil.parseToLocalDateTime(request.getEndTime());

            event.setTitle(request.getTitle());
            event.setDescription(request.getDescription());
            event.setLocation(request.getLocation());
            event.setStartTime(DateTimeUtil.convertLocalDateTimeToInstant(startTime));
            event.setEndTime(DateTimeUtil.convertLocalDateTimeToInstant(endTime));
            event.setVisibility(EventVisibilityType.valueOf(request.getEventVisibilityType().toUpperCase()));
            event.setHost(userService.getHost(request.getHostId()));
            return event;
    }

}
