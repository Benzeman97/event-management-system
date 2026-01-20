@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        
        switch (response.status()) {
            case 400:
                return new IllegalArgumentException("Bad request from " + methodKey);
            case 404:
                return new RuntimeException("Resource not found in " + methodKey);
            case 500:
                return new RuntimeException("Internal server error from " + methodKey);
            default:
                return new RuntimeException("Feign error: " + response.status());
        }
    }
}
