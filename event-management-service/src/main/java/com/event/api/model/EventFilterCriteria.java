package com.event.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFilterCriteria {

    private String startDate;
    private String endDate;
    private String location;
    private String visibility;
}
