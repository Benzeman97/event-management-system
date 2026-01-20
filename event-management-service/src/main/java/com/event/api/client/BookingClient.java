@FeignClient(name = "booking-service")
public interface BookingClient {

    @PostMapping("/api/bookings")
    BookingResponse createBooking(@RequestBody BookingRequest request);
}
