package com.event.api.controller;

import com.event.api.dto.request.CreateEventRequest;
import com.event.api.dto.request.UpdateEventRequest;
import com.event.api.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

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

}
