package lovable_clone.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lovable_clone.dto.member.InviteMemberRequest;
import lovable_clone.dto.member.MemberResponse;
import lovable_clone.dto.member.updateMemberRoleRequest;
import lovable_clone.entity.Project;
import lovable_clone.entity.ProjectMember;
import lovable_clone.entity.ProjectMemberId;
import lovable_clone.entity.User;
import lovable_clone.error.ResourceNotFoundException;
import lovable_clone.mapper.ProjectMapper;
import lovable_clone.mapper.ProjectMemberMapper;
import lovable_clone.repository.ProjectMemberRepository;
import lovable_clone.repository.ProjectRepository;
import lovable_clone.repository.UserRepository;
import lovable_clone.security.AuthUtil;
import lovable_clone.service.ProjectMemberService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Transactional
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {

    ProjectMemberRepository projectMemberRepository;
    ProjectRepository projectRepository;
    ProjectMemberMapper projectMemberMapper;
    UserRepository userRepository;
    ProjectMapper projectMapper;
    AuthUtil authUtil;

    @Override
    @PreAuthorize("@security.canViewMembers(#projectId)")
    public List<MemberResponse> getProjectMembers(Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProjectById(projectId, userId);

        return projectMemberRepository.findByIdProjectId(projectId)
                .stream()
                .map(projectMemberMapper::toProjectMemberResponseFromMember)
                .toList();
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProjectById(projectId,userId);


        User invitee = userRepository.findByUsername(request.username()).orElseThrow();
        if(invitee.getId().equals(userId)){
            throw new RuntimeException("Cannot Invite Yourself");
        }
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId,invitee.getId());
        if(projectMemberRepository.existsById(projectMemberId)){
            throw new RuntimeException("Already Exists");
        }
        ProjectMember member = ProjectMember.builder()
                .id(projectMemberId)
                .project(project)
                .user(invitee)
                .projectRole(request.role())
                .invitedAt(Instant.now())
                .build();
        projectMemberRepository.save(member);
        return projectMemberMapper.toProjectMemberResponseFromMember(member);
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public MemberResponse updateMemberRole(Long projectId, updateMemberRoleRequest request, Long memberId) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProjectById(projectId, userId);



//        if (memberId.equals(project.getOwner().getId())) {
//            throw new RuntimeException("Cannot update the role of the project owner");
//        }

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        ProjectMember member = projectMemberRepository.findById(projectMemberId)
                .orElseThrow(() -> new RuntimeException("Member not found in this project"));

        member.setProjectRole(request.role());

        ProjectMember savedMember = projectMemberRepository.save(member);
        return projectMemberMapper.toProjectMemberResponseFromMember(savedMember);
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public void removeProjectMember(Long projectId, Long memberId) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProjectById(projectId, userId);



        if (memberId.equals(userId)) {
            throw new RuntimeException("Self-removal is not allowed");
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        ProjectMember member = projectMemberRepository.findById(projectMemberId)
                .orElseThrow(() -> new RuntimeException("Member not found in this project"));

        projectMemberRepository.delete(member);
    }

    /// Internal Functions
    public Project getAccessibleProjectById(Long projectId, Long userId){
        return projectRepository.findAccessibleProjectById(projectId,userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId.toString()));
    }
}
