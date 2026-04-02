package lovable_clone.dto.project;

import lovable_clone.dto.authdto.UserProfileResponse;

import java.time.Instant;

public record ProjectResponse (
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt,
        UserProfileResponse owner
){
}
