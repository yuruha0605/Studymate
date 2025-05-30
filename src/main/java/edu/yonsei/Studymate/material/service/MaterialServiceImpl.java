package edu.yonsei.Studymate.material.service;

import edu.yonsei.Studymate.material.dto.MaterialDto;
import edu.yonsei.Studymate.material.dto.MaterialFileDto;
import edu.yonsei.Studymate.material.entity.MaterialEntity;
import edu.yonsei.Studymate.material.entity.MaterialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public MaterialDto uploadMaterial(MultipartFile file, MaterialDto materialDTO) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            MaterialEntity material = MaterialEntity.builder()
                    .title(materialDTO.getTitle())
                    .description(materialDTO.getDescription())
                    .fileName(fileName)
                    .originalFileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .filePath(filePath.toString())
                    .studygroupId(materialDTO.getStudygroupId())
                    .creatorId(materialDTO.getCreatorId())  // 추가
                    .build();

            MaterialEntity savedMaterial = materialRepository.save(material);
            return convertToDto(savedMaterial);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }


    @Override
    public List<MaterialDto> getMaterialsByStudygroup(Long studygroupId) {
        List<MaterialEntity> materials = materialRepository.findByStudygroupId(studygroupId);
        return materials.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MaterialFileDto downloadMaterial(Long materialId) {
        MaterialEntity material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다."));

        try {
            Path filePath = Paths.get(material.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return MaterialFileDto.builder()
                        .fileName(material.getOriginalFileName())
                        .contentType(material.getContentType())
                        .resource(resource)
                        .build();
            } else {
                throw new RuntimeException("파일을 찾을 수 없습니다.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("파일 다운로드에 실패했습니다.", e);
        }
    }

    private MaterialDto convertToDto(MaterialEntity entity) {
        return MaterialDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .fileName(entity.getFileName())
                .studygroupId(entity.getStudygroupId())
                .creatorId(entity.getCreatorId())
                .build();
    }

    @Override
    public MaterialDto updateMaterial(Long materialId, MaterialDto materialDto, MultipartFile file) {
        MaterialEntity material = materialRepository.findById(materialId)
                .orElseThrow(() -> new EntityNotFoundException("자료를 찾을 수 없습니다."));

        material.setTitle(materialDto.getTitle());
        material.setDescription(materialDto.getDescription());

        // 새로운 파일이 업로드된 경우
        if (file != null && !file.isEmpty()) {
            try {
                // 기존 파일 삭제
                Path oldFilePath = Paths.get(material.getFilePath());
                Files.deleteIfExists(oldFilePath);

                // 새 파일 저장
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // 파일 정보 업데이트
                material.setFileName(fileName);
                material.setOriginalFileName(file.getOriginalFilename());
                material.setContentType(file.getContentType());
                material.setFilePath(filePath.toString());
            } catch (IOException e) {
                throw new RuntimeException("파일 업데이트 중 오류가 발생했습니다.", e);
            }
        }

        MaterialEntity updatedMaterial = materialRepository.save(material);
        return convertToDto(updatedMaterial);
    }


    @Override
    public void deleteMaterial(Long materialId) {
        MaterialEntity material = materialRepository.findById(materialId)
                .orElseThrow(() -> new EntityNotFoundException("자료를 찾을 수 없습니다."));

        // 파일 시스템에서 파일 삭제
        try {
            Path filePath = Paths.get(material.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 중 오류가 발생했습니다.", e);
        }

        materialRepository.delete(material);
    }

    @Override
    public MaterialDto getMaterial(Long materialId) {
        MaterialEntity material = materialRepository.findById(materialId)
                .orElseThrow(() -> new EntityNotFoundException("자료를 찾을 수 없습니다."));
        return convertToDto(material);
    }



}