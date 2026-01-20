@FeignClient(name = "user-service")
public interface UserClient {

   @GetMapping("/api/users/{id}")
   UserResponse getUser(@PathVariable Long id);
  
}
