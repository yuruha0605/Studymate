package edu.yonsei.Studymate.main.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MainPageDto {
    private String loginId;
    private String name;
    private String studentId;
    private String studyTime;
    private String profileImageUrl;
    private List<String> enrolledSubjects;
    private List<String> activeSchedules;  // Assignment를 Schedule로 변경
}
