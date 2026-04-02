package lovable_clone.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lovable_clone.dto.authdto.AuthResponse;
import lovable_clone.dto.authdto.LoginRequest;
import lovable_clone.dto.authdto.SignupRequest;
import lovable_clone.dto.authdto.UserProfileResponse;
import lovable_clone.service.AuthService;
import lovable_clone.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {

    AuthService authService;
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request){
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(){
        Long userId = 1L;
        return ResponseEntity.ok(userService.getProfile(userId));
    }
}
