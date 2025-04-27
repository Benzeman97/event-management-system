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
public class UpdateEventRequest implements Serializable {

    @NotBlank(message = "{event.eventId.required}")
    private String eventId;
    @NotBlank(message = "{event.title.required}")
    @Size(min = 10, max = 100, message = "{event.title.length.invalid}")
    private String title;
    @NotBlank(message = "{event.description.required}")
    @Size(min = 10, max = 1000, message = "{event.description.length.invalid}")
    private String description;
    @NotBlank(message = "{event.hostId.required}")
    private String hostId;
    @NotBlank(message = "{event.location.required}")
    private String location;
    @NotBlank(message = "{event.startTime.required}")
    private String startTime;
    @NotBlank(message = "{event.endTime.required}")
    private String endTime;
    private String eventVisibilityType;

}
