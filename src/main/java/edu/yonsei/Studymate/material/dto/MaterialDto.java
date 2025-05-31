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
    private Long creatorId;  // 추가
    private String originalFileName;  // 원본 파일명 추가
    private String contentType;      // 파일 타입 추가
    private LocalDateTime uploadedAt;  // 추가
    private String creatorEmail; // 추가 (화면 표시용)



}

