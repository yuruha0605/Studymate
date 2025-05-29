package edu.yonsei.Studymate.material.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialFileDto {
    private String fileName;
    private String contentType;
    private Resource resource;
}

