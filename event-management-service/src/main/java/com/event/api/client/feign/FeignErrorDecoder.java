package com.example.orderservice.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        String message = "Feign error in " + methodKey + " with status " + response.status();

        switch (response.status()) {
            case 400:
                return new IllegalArgumentException("Bad request: " + message);
            case 404:
                if (methodKey.contains("UserClient")) {
                    return new UserServiceException(message);
                } else if (methodKey.contains("PaymentClient")) {
                    return new PaymentServiceException(message);
                } else if (methodKey.contains("BookingClient")) {
                    return new BookingServiceException(message);
                } else {
                    return new RuntimeException(message);
                }
            case 500:
                if (methodKey.contains("PaymentClient")) {
                    return new PaymentServiceException(message);
                }
                return new RuntimeException("Internal server error: " + message);
            default:
                return new RuntimeException(message);
        }
    }
}
