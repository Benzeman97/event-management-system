package com.event.api.dto.response;

import com.event.api.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDetailsResponse implements Serializable {

    private Event event;
    private int attendeeCount;
}
