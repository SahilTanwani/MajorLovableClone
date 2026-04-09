package lovable_clone.security;

import lombok.RequiredArgsConstructor;
import lovable_clone.enums.ProjectPermission;
import lovable_clone.enums.ProjectRole;
import lovable_clone.repository.ProjectMemberRepository;
import org.springframework.stereotype.Component;

import java.security.Permission;

@Component("security")
@RequiredArgsConstructor
public class SecurityExpressions {
    private final ProjectMemberRepository projectMemberRepository;
    private final AuthUtil authUtil;

    private boolean hasPermission(Long projectId, ProjectPermission permission) {
        long userId = authUtil.getCurrentUserId();
        return projectMemberRepository.findRoleByProjectIdAndUserId(projectId, userId).
                map(role -> role.getPermissions().contains(permission)).
                orElse(false);
    }

    public boolean canViewProject(Long projectId) {
        long userId = authUtil.getCurrentUserId();
        return hasPermission(projectId, ProjectPermission.VIEW);

    }
    public boolean canEditProject(Long projectId) {
        long userId = authUtil.getCurrentUserId();
        return hasPermission(projectId, ProjectPermission.EDIT);
    }
    public boolean canDeleteProject(Long projectId) {
        long userId = authUtil.getCurrentUserId();
        return hasPermission(projectId, ProjectPermission.DELETE);
    }
    public boolean canViewMembers(Long projectId) {
        long userId = authUtil.getCurrentUserId();
        return hasPermission(projectId, ProjectPermission.VIEW_MEMBERS);
    }
    public boolean canManageMembers(Long projectId) {
        return hasPermission(projectId, ProjectPermission.MANAGE_MEMBERS);
    }
}
