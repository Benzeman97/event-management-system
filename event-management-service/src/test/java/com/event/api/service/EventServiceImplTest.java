package com.event.api.service;

import com.event.api.dto.request.CreateEventRequest;
import com.event.api.entity.Attendance;
import com.event.api.entity.AttendanceId;
import com.event.api.entity.Event;
import com.event.api.entity.User;
import com.event.api.enums.AttendanceStatusType;
import com.event.api.exception.ApplicationException;
import com.event.api.repository.EventRepository;
import com.event.api.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventServiceImplTest")
public class EventServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    @DisplayName("createEventTest")
    public void createEventTest(){

        UUID hostId = UUID.randomUUID();

        CreateEventRequest request = buildEventRequest(hostId);

        User mockHost = new User();
        mockHost.setId(hostId);

        UUID eventId=UUID.randomUUID();

        Event mockEvent = new Event();
        mockEvent.setId(eventId);
        mockEvent.setHost(mockHost);
        Set<Attendance> attendances = new HashSet<>();
        attendances.add(buildAttendance(mockEvent,mockHost));
        mockEvent.setAttendances(attendances);

        Mockito.when(userService.getHost(request.getHostId())).thenReturn(mockHost);
        Mockito.when(eventRepository.save(Mockito.any(Event.class))).thenReturn(mockEvent);

        String result = eventService.createEvent(request);

        Assertions.assertEquals(eventId.toString(),result);
        Mockito.verify(eventRepository, Mockito.times(1)).save(Mockito.any(Event.class));

    }

    @Test
    @DisplayName("createEvent_InvalidDateFormat_ThrowsException_Test")
    public void createEvent_InvalidDateFormat_ThrowsException_Test() {

        CreateEventRequest request = new CreateEventRequest();
        request.setStartTime("invalid-date");  // Bad date


        ApplicationException ex = Assertions.assertThrows(ApplicationException.class, () -> {
            eventService.createEvent(request);
        });

        Assertions.assertEquals(100003, ex.getErrorCode());
    }

    private CreateEventRequest buildEventRequest(UUID hostId) {
        CreateEventRequest request = new CreateEventRequest();
        request.setTitle("Music Party");
        request.setDescription("Year end annual party");
        request.setLocation("Colombo");
        request.setStartTime("2025-04-30T18:00:00");
        request.setEndTime("2025-04-30T23:00:00");
        request.setHostId(hostId.toString());
        request.setEventVisibilityType("PUBLIC");
        return request;
    }

    private Attendance buildAttendance(Event event,User user) {
        Attendance attendance = new Attendance();
        attendance.setId(new AttendanceId(event.getId(),user.getId()));
        attendance.setEvent(event);
        attendance.setUser(user);
        attendance.setStatus(AttendanceStatusType.GOING);
        attendance.setRespondedAt(Instant.now());
        return attendance;
    }
}
