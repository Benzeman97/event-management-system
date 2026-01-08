package com.event.api.mapper;

import com.event.api.entity.Event;

public class EventMapper{


    public static Event createEventFromRequest(CreateEventRequest request, User host){
      
             LocalDateTime startTime = DateTimeUtil.parseToLocalDateTime(request.getStartTime());
             LocalDateTime endTime = DateTimeUtil.parseToLocalDateTime(request.getEndTime());

             Event event = new Event();
             event.setTitle(request.getTitle());
             event.setDescription(request.getDescription());
             event.setLocation(request.getLocation());
             event.setStartTime(DateTimeUtil.convertLocalDateTimeToInstant(startTime));
             event.setEndTime(DateTimeUtil.convertLocalDateTimeToInstant(endTime));
             event.setVisibility(EventVisibilityType.valueOf(request.getEventVisibilityType().toUpperCase()));
             event.setHost(host);
      
             event.getAttendances().add(handleHostAttendance(event,host));

            return event;
    }

     public static Event updateEventFromRequest(UpdateEventRequest request,User host, Event event) {
       
             LocalDateTime startTime = DateTimeUtil.parseToLocalDateTime(request.getStartTime());
             LocalDateTime endTime = DateTimeUtil.parseToLocalDateTime(request.getEndTime());

            event.setTitle(request.getTitle());
            event.setDescription(request.getDescription());
            event.setLocation(request.getLocation());
            event.setStartTime(DateTimeUtil.convertLocalDateTimeToInstant(startTime));
            event.setEndTime(DateTimeUtil.convertLocalDateTimeToInstant(endTime));
            event.setVisibility(EventVisibilityType.valueOf(request.getEventVisibilityType().toUpperCase()));
            event.setHost(host);
       
            event.getAttendances().add(handleHostAttendance(event,host));
            return event;
       
     }

}

