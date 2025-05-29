package edu.yonsei.Studymate.material.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MaterialDto {
    private Long id;
    private String title;
    private String description;
    private String fileName;
    private Long studygroupId;
    private LocalDateTime uploadDateTime;
}

