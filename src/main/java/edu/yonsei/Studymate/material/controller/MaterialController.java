package edu.yonsei.Studymate.material.controller;

import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.repository.UserRepository;
import edu.yonsei.Studymate.material.dto.MaterialDto;
import edu.yonsei.Studymate.material.dto.MaterialFileDto;
import edu.yonsei.Studymate.material.entity.MaterialEntity;
import edu.yonsei.Studymate.material.entity.MaterialRepository;
import edu.yonsei.Studymate.material.service.MaterialService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/study-mate/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;
    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("studygroupId") Long studygroupId,
            @RequestParam("creatorId") Long creatorId) {
        try {
            MaterialDto materialDTO = MaterialDto.builder()
                    .title(title)
                    .description(description)
                    .studygroupId(studygroupId)
                    .creatorId(creatorId)
                    .uploadedAt(LocalDateTime.now()) // 현재 시간 추가
                    .build();

            MaterialDto savedMaterial = materialService.uploadMaterial(file, materialDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "자료가 성공적으로 업로드되었습니다.");
            response.put("data", savedMaterial);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



    @GetMapping("/{materialId}")
    public ResponseEntity<MaterialDto> getMaterial(@PathVariable Long materialId) {
        try {
            MaterialDto material = materialService.getMaterial(materialId);
            return ResponseEntity.ok(material);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }



    @GetMapping("/studygroup/{studygroupId}")
    public ResponseEntity<List<MaterialDto>> getMaterialsByStudygroup(
            @PathVariable Long studygroupId) {
        List<MaterialDto> materials = materialService.getMaterialsByStudygroup(studygroupId);
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/download/{materialId}")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable Long materialId) {
        MaterialFileDto fileDTO = materialService.downloadMaterial(materialId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDTO.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileDTO.getFileName() + "\"")
                .body(fileDTO.getResource());
    }

    @PutMapping("/{materialId}")
    public ResponseEntity<MaterialDto> updateMaterial(
            @PathVariable Long materialId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {

        MaterialDto materialDto = MaterialDto.builder()
                .id(materialId)
                .title(title)
                .description(description)
                .build();

        MaterialDto updatedMaterial = materialService.updateMaterial(materialId, materialDto, file);
        return ResponseEntity.ok(updatedMaterial);
    }


    @DeleteMapping("/{materialId}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long materialId) {
        materialService.deleteMaterial(materialId);
        return ResponseEntity.noContent().build();
    }

}
