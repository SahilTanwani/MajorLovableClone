package lovable_clone.dto.member;

import jakarta.validation.constraints.NotNull;
import lovable_clone.enums.ProjectRole;

public record updateMemberRoleRequest(@NotNull ProjectRole role) {
}
