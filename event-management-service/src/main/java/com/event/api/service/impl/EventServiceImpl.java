package com.event.api.service.impl;

import com.event.api.dto.request.CreateEventRequest;
import com.event.api.dto.request.UpdateEventRequest;
import com.event.api.entity.Event;
import com.event.api.enums.EventVisibilityType;
import com.event.api.exception.ApplicationException;
import com.event.api.exception.DataNotFoundException;
import com.event.api.model.EventFilterCriteria;
import com.event.api.repository.EventRepository;
import com.event.api.service.EventService;
import com.event.api.service.UserService;
import com.event.api.util.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
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
            LOGGER.error("An error occurred while creating event", ex);
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
               LOGGER.info("Event updated successfully with ID {}", request.getEventId());
                return event.getId().toString();
        } catch (DateTimeParseException ex) {
            LOGGER.error("Invalid date format for event with ID {}", request.getEventId());
            throw new ApplicationException(100003, "error.invalid.date.format");
        } catch (Exception ex){
            LOGGER.error("An error occurred while updating event with ID {}", request.getEventId());
            throw new ApplicationException(100002, "error.update.event");
        }

    }

    @Override
    public void deleteEvent(UUID eventId) {
       try {
           Event event = eventRepository.findById(eventId)
                   .orElseThrow(() -> {
                       LOGGER.error("Event with ID {} not found", eventId);
                       throw new DataNotFoundException("error.data.not.found");
                   });
           eventRepository.delete(event);
           LOGGER.info("Event deleted successfully with ID {}", eventId);
       } catch (Exception ex){
           LOGGER.error("An error occurred while deleting event with ID {}", eventId);
           throw new ApplicationException(100007, "error.delete.event");
       }
    }

    @Override
    public List<Event> getFilteredEvents(EventFilterCriteria filterCriteria) {

        Instant startDate = null;
        Instant endDate = null;
        EventVisibilityType visibility = null;

        try {
            if (filterCriteria.getStartDate() != null && !filterCriteria.getStartDate().isBlank()) {
                startDate = DateTimeUtil.parseToInstant(filterCriteria.getStartDate());
            }
            if (filterCriteria.getEndDate() != null && !filterCriteria.getEndDate().isBlank()) {
                endDate = DateTimeUtil.parseToInstant(filterCriteria.getEndDate());
            }
            if (filterCriteria.getVisibility() != null && !filterCriteria.getVisibility().isBlank()) {
                visibility = EventVisibilityType.valueOf(filterCriteria.getVisibility().toUpperCase());
            }
            return eventRepository.findByFilters(startDate, endDate, filterCriteria.getLocation(), visibility);
        } catch (DateTimeParseException ex){
            LOGGER.error("Invalid date format for event", ex);
            throw new ApplicationException(100003, "error.invalid.date.format");
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Invalid visibility type for event", ex);
            throw new ApplicationException(100006, "error.invalid.visibility.type");
        } catch (Exception ex){
            LOGGER.error("An error occurred while filtering event", ex);
            throw new ApplicationException(100005, "error.filter.event");
        }
    }

    @Override
    public Page<Event> getUpcomingEvents(Pageable pageable) {
        Instant currentTime = Instant.now();
        return eventRepository.findUpcomingEvents(currentTime, pageable);
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
