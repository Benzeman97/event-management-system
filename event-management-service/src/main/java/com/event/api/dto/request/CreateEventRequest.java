package com.event.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest implements Serializable {

    @NotBlank(message = "{CreateEventRequest.title.required}")
    @Size(min = 10, max = 100, message = "{CreateEventRequest.title.length.invalid}")
    private String title;
    @NotBlank(message = "{CreateEventRequest.description.required}")
    @Size(min = 10, max = 1000, message = "{CreateEventRequest.description.length.invalid}")
    private String description;
    @NotBlank(message = "{CreateEventRequest.hostId.required}")
    private String hostId;
    @NotBlank(message = "{CreateEventRequest.location.required}")
    private String location;
    @NotBlank(message = "{CreateEventRequest.startTime.required}")
    private String startTime;
    @NotBlank(message = "{CreateEventRequest.endTime.required}")
    private String endTime;
    private String eventVisibilityType;
}
