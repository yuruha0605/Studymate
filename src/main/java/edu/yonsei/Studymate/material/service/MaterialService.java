package edu.yonsei.Studymate.material.service;

import edu.yonsei.Studymate.material.dto.MaterialDto;
import edu.yonsei.Studymate.material.dto.MaterialFileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface MaterialService {
    MaterialDto uploadMaterial(MultipartFile file, MaterialDto materialDTO);
    List<MaterialDto> getMaterialsByStudygroup(Long studygroupId);
    MaterialFileDto downloadMaterial(Long materialId);
    MaterialDto updateMaterial(Long materialId, MaterialDto materialDto, MultipartFile file);
    void deleteMaterial(Long materialId);
    MaterialDto getMaterial(Long materialId);

}


