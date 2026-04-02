package lovable_clone.dto.authdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank String username,
        @Size(min = 1,max = 30) String name,
        @Size(min = 4,max = 50) String password
) {
}
