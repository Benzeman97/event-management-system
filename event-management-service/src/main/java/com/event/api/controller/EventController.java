package com.event.api.controller;

import com.event.api.dto.request.CreateEventRequest;
import com.event.api.dto.request.UpdateEventRequest;
import com.event.api.entity.Event;
import com.event.api.exception.ApplicationException;
import com.event.api.model.EventFilterCriteria;
import com.event.api.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService){
        this.eventService=eventService;
    }

    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody CreateEventRequest request){
             return new ResponseEntity<>(eventService.createEvent(request), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateEvent(@RequestBody UpdateEventRequest request){
        return new ResponseEntity<>(eventService.updateEvent(request), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventId){
         if(eventId.isBlank())
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
         try {
             eventService.deleteEvent(UUID.fromString(eventId));
             return ResponseEntity.noContent().build();
         }catch (IllegalArgumentException ex){
             throw new ApplicationException(100004,"error.invalid.uuid.format");
         }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Event>> listEvents(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String visibility) {

        EventFilterCriteria filterCriteria = new EventFilterCriteria(startDate, endDate, location, visibility);
        return new ResponseEntity<>(eventService.getFilteredEvents(filterCriteria),HttpStatus.OK);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Page<Event>> getUpcomingEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(eventService.getUpcomingEvents(pageable),HttpStatus.OK);
    }
}
