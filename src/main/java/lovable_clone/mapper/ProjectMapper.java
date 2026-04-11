package lovable_clone.mapper;

import lovable_clone.dto.project.ProjectResponse;
import lovable_clone.dto.project.ProjectSummaryResponse;
import lovable_clone.entity.Project;
import lovable_clone.enums.ProjectRole;
import lovable_clone.repository.ProjectRepository;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toProjectResponse(Project project);

    ProjectSummaryResponse toProjectSummaryResponse(Project project, ProjectRole role);

    List<ProjectSummaryResponse> toListOfProjectSummaryResponse(List<ProjectRepository.ProjectWithRole> projects);
}
