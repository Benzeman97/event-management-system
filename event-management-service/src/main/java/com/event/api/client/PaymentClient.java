@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/api/payments")
    PaymentResponse createPayment(@RequestBody PaymentRequest request);
}
