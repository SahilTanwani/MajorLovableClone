package lovable_clone.service;

import lovable_clone.dto.authdto.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(Long userId);
}
