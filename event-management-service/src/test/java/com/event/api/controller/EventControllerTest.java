package com.event.api.controller;

import com.event.api.dto.request.CreateEventRequest;
import com.event.api.entity.Attendance;
import com.event.api.entity.AttendanceId;
import com.event.api.entity.Event;
import com.event.api.entity.User;
import com.event.api.enums.AttendanceStatusType;
import com.event.api.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.UUID;

@WebMvcTest(EventController.class)
@DisplayName("EventControllerTest")
public class EventControllerTest {

    @MockBean
    private EventService eventService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("createEventTest")
    public void createEventTest() throws Exception {

        UUID eventId=UUID.randomUUID();
        UUID hostId = UUID.randomUUID();

        CreateEventRequest request = buildEventRequest(hostId);

        Mockito.when(eventService.createEvent(request)).thenReturn(eventId.toString());

        MvcResult result = mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(asJsonString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(eventId.toString())).andReturn();

        int status = result.getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.CREATED.value(),status);

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

    private static String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }
}
