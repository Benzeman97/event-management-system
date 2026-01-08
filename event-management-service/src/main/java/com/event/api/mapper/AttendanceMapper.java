

public class AttendanceMapper {

  public static Attendance handleHostAttendance(Event event, User user){
    
        Attendance hostAttendance = new Attendance();
        hostAttendance.setId(new AttendanceId(event.getId(),user.getId()));
        hostAttendance.setEvent(event);
        hostAttendance.setUser(user);
        hostAttendance.setStatus(AttendanceStatusType.GOING); // Default status for host
        hostAttendance.setRespondedAt(Instant.now());
        return hostAttendance;
    }

}
