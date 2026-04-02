package lovable_clone.mapper;

import lovable_clone.dto.authdto.SignupRequest;
import lovable_clone.dto.authdto.UserProfileResponse;
import lovable_clone.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(SignupRequest signupRequest);

    UserProfileResponse toUserProfileResponse(User user);
}
