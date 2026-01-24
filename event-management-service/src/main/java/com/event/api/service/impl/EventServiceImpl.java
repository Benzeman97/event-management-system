package com.event.api.service.impl;

import com.event.api.dto.request.CreateEventRequest;
import com.event.api.dto.request.UpdateEventRequest;
import com.event.api.dto.response.EventDetailsResponse;
import com.event.api.entity.Attendance;
import com.event.api.entity.AttendanceId;
import com.event.api.entity.Event;
import com.event.api.entity.User;
import com.event.api.enums.AttendanceStatusType;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    final private static Logger LOGGER = LogManager.getLogger(EventServiceImpl.class);

    private final UserService userService;
    private final EventRepository eventRepository;
    private final UserClient userClient;
    private final PaymentClient paymentClient;
    private final BookingClient bookingClient;

    
    @Override
    @Transactional
    public String createEvent(CreateEventRequest request){

        try {
            User host = userService.getHost(request.getHostId());
            Event event = EventMapper.createEventFromRequest(request,host);
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
    @Transactional
    public String updateEvent(UpdateEventRequest request) {
        try{

              Event event = eventRepository.findByIdForUpdate(UUID.fromString(request.getEventId()))
                      .orElseThrow(()->{
                          LOGGER.error("Event with ID {} not found", request.getEventId());
                          throw new DataNotFoundException("error.data.not.found");
                      });
               User host = userService.getHost(request.getHostId());
               event = EventMapper.updateEventFromRequest(request,event,host);
               eventRepository.save(event);
               LOGGER.info("Event updated successfully with ID {}", request.getEventId());
               evictEventCaches(request.getId());
         u     updateEventCache(event.getId(), event);
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
    @CacheEvict(value = {"event:single", "event:list", "event:page"}, key = "#eventId")
    @Transactional // default, goes to MASTER
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
    @Cacheable( value = "event:list",
            key = "#filterCriteria.startDate + '-' + #filterCriteria.endDate + '-' + #filterCriteria.location + '-' + #filterCriteria.visibility")
    @Transactional(readOnly = true) // ensures read goes to SLAVE
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
    @Cacheable(value = "event:page", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<Event> getUpcomingEvents(Pageable pageable) {
        Instant currentTime = Instant.now();
        LOGGER.info("Fetching Upcoming Events - Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return eventRepository.findUpcomingEvents(currentTime, pageable);
    }

    @Override
    @Cacheable(value = "event:list", key = "#userId")
    @Transactional(readOnly = true)
    public List<Event> getUserEvents(UUID userId) {
        LOGGER.info("Fetching Events for User ID {}", userId);
        return eventRepository.findUserEventsByUserId(userId);
    }

    @Override
    @Cacheable(value = "event.single", key = "#eventId")
    @Transactional(readOnly = true)
    public EventDetailsResponse getEventDetails(UUID eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    LOGGER.error("Event with ID {} not found", eventId);
                    throw new DataNotFoundException("error.event.not.found");});
        int attendeeCount = event.getAttendances().size();  // counting the attendances
        LOGGER.info("Fetching Event Details with ID {}", eventId);
        return new EventDetailsResponse(event, attendeeCount);
    }

    @CacheEvict(value = "event:list", key = "#userId")
    private void evictEventList(String userId) {}

    @CacheEvict(value = "event:page", key = "#userId")
    private void evictEventPage(String userId) {}

    private void evictUserCaches(String eventId) {
       evictEventList(eventId);
        evictEventPage(eventId);
    }

    @CachePut(value = "event:single", key = "#eventId")
    private Event updateEventCache(String eventId, Event event) {
         return event; // This caches the Event object
   }
    
  /*  public EventResponse placeOrder(Long userId, OrderRequest request) {

        UserDto user = userClient.getUser(userId);

        PaymentResponse payment =
                paymentClient.createPayment(request.getPayment());

        BookingResponse booking =
                bookingClient.createBooking(request.getBooking());

        return new EventResponse(user, payment, booking);
    } */

    /* 
    @Override
    @Cacheable(value = "payment:single", key = "#request.id")  // check cache first
    @CircuitBreaker(name = "paymentService", fallbackMethod = "fallback")  // Fast fail (prevents retries when circuit open)
    @Retry(name = "paymentService")  // retry on failure (Only retries when circuit is closed)
    @Transactional(readOnly = true) // DB transaction (if needed) (Expensive (database connection))
    public PaymentResponse createPayment(PaymentRequest request) {
        return paymentClient.createPayment(request);
    }
    
     private PaymentResponse createPaymentFallback(PaymentRequest request, Exception ex) {
        LOGGER.error("Payment service unavailable for request: {}", request.getId(), ex);
        return PaymentResponse.builder()
                .status("FAILED")
                .message("Service temporarily unavailable. Please try again later.")
                .build();
    }
    
    */


}
