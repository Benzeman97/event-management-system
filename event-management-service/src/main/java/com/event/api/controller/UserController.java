
@RestController
@RequestMapping("/api")
public class UserController {

     private final UserController userController;

     public UserController(UserController userController){
            this.userController = userController;
     }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable int userId){
          return ResponseEntity.ok(patientService.getPatientById(id));
    }

    
}
