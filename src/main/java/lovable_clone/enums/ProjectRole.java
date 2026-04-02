package lovable_clone.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static lovable_clone.enums.ProjectPermission.*;

import java.util.Set;
@RequiredArgsConstructor
@Getter
public enum ProjectRole {
    EDITOR(Set.of(EDIT,DELETE,VIEW,VIEW_MEMBERS)),
    VIEWER(Set.of(VIEW,VIEW_MEMBERS)),
    OWNER(Set.of(EDIT,DELETE,VIEW,MANAGE_MEMBERS,VIEW_MEMBERS));

    private final Set<ProjectPermission> permissions;

    }
