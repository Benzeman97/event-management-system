
@RestController
@RequestMapping("/api")
public class UserController {

     private final UserController userController;
     private final AsyncEmailService asyncEmailService;

     public UserController(UserController userController, AsyncEmailService asyncEmailService){
            this.userController = userController;
            this.asyncEmailService = asyncEmailService;
     }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable int userId){
          return ResponseEntity.ok(patientService.getPatientById(id));
    }


  @PostMapping("/sendEmail")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        // Email is sent in background, API responds immediately
        asyncEmailService.sendEmailAsync(emailRequest);
        return ResponseEntity.ok("Email is being sent");
    }

     /**
     * Send email with result tracking
     */
    @PostMapping("/sendEmailWithTracking")
    public CompletableFuture<ResponseEntity<String>> sendEmailWithTracking(
            @RequestBody EmailRequest emailRequest) {
        
        return asyncEmailService.sendEmailWithResult(emailRequest)
            .thenApply(success -> {
                if (success) {
                    return ResponseEntity.ok("Email sent successfully");
                } else {
                    return ResponseEntity.status(500).body("Email sending failed");
                }
            });
    }

     /**
     * Example: Register user and send welcome email asynchronously
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserResponse userRequest) {
        // Save user (fast operation)
        UserResponse user = userService.getUserById(1); // Simulating user creation
        
        // Send welcome email asynchronously (slow operation runs in background)
        EmailRequest welcomeEmail = new EmailRequest(
            user.getEmail(),
            "Welcome to our platform!",
            "Hello " + user.getName() + ", welcome aboard!"
        );
        asyncEmailService.sendEmailAsync(welcomeEmail);
        
        // Return response immediately without waiting for email
        return ResponseEntity.ok(user);
    }
    
}
