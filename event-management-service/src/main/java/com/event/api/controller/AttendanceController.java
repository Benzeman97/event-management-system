package com.event.api.controller;

import com.event.api.exception.ApplicationException;
import com.event.api.service.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/v1/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService){
        this.attendanceService=attendanceService;
    }

    @GetMapping("/event/{eventId}/user/{userId}/status")
    public ResponseEntity<String> getAttendanceStatusForEvent(@PathVariable String eventId, @PathVariable String userId) {

        try{
        if(eventId.isBlank() || userId.isBlank())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
         return new ResponseEntity<>(attendanceService.getAttendanceStatusForEvent(UUID.fromString(eventId),UUID.fromString(userId)),HttpStatus.OK);
        } catch (IllegalArgumentException ex){
            throw new ApplicationException(100004,"error.invalid.uuid.format");
        }
    }
}
