package edu.yonsei.Studymate.material.controller;

import edu.yonsei.Studymate.material.dto.MaterialDto;
import edu.yonsei.Studymate.material.dto.MaterialFileDto;
import edu.yonsei.Studymate.material.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/study-mate/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("studygroupId") Long studygroupId) {

        MaterialDto materialDTO = MaterialDto.builder()
                .title(title)
                .description(description)
                .studygroupId(studygroupId)
                .build();

        MaterialDto savedMaterial = materialService.uploadMaterial(file, materialDTO);
        return ResponseEntity.ok(savedMaterial);
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
}
