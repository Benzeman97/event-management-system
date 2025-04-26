package com.event.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CreateEventRequest implements Serializable {

    private String title;
    private String description;
    private String location;
    private String startTime;
    private String endTime;
    private String eventVisibilityType;
}
