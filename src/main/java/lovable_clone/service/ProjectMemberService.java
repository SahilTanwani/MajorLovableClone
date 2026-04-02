package lovable_clone.service;

import lovable_clone.dto.member.InviteMemberRequest;
import lovable_clone.dto.member.MemberResponse;
import lovable_clone.dto.member.updateMemberRoleRequest;


import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(Long projectId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request);

    MemberResponse updateMemberRole(Long projectId, updateMemberRoleRequest request, Long memberId);

    void removeProjectMember(Long projectId, Long memberId);
}
