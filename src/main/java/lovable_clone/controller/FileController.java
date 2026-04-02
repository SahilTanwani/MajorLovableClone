package lovable_clone.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lovable_clone.dto.project.FileContentResponse;
import lovable_clone.dto.project.FileNode;
import lovable_clone.dto.project.FileTreeResponse;
import lovable_clone.service.ProjectFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/projects/{projectId}/files")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileController {
    ProjectFileService projectFileService;

    @GetMapping
    public ResponseEntity<FileTreeResponse> getFileTree(@PathVariable Long projectId){
        return ResponseEntity.ok(projectFileService.getFileTree(projectId));
    }

//    @GetMapping("/{*path}")
//    public ResponseEntity<FileContentResponse> getFile(
//            @PathVariable Long projectId,
//            @PathVariable String path
//    ){
//        Long userId = 1L;
//        return ResponseEntity.ok(projectFileService.getFileContent(projectId,path));
//    }

    @GetMapping("/content")
    public ResponseEntity<FileContentResponse> getFile(
            @PathVariable Long projectId,
            @RequestParam String path) {
        return ResponseEntity.ok(projectFileService.getFileContent(projectId, path));
    }


}
