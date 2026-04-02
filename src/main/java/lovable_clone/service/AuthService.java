package lovable_clone.service;

import lovable_clone.dto.authdto.AuthResponse;
import lovable_clone.dto.authdto.LoginRequest;
import lovable_clone.dto.authdto.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
