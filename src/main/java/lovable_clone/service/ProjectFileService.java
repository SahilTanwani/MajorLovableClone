package lovable_clone.service;

import lovable_clone.dto.project.FileContentResponse;
import lovable_clone.dto.project.FileNode;
import lovable_clone.dto.project.FileTreeResponse;

import java.util.List;

public interface ProjectFileService {
    FileTreeResponse getFileTree(Long projectId);

    FileContentResponse getFileContent(Long projectId, String path);
    void saveFile(Long projectId, String filePath, String fileContent);
}
