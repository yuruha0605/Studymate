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
    private List<String> activeSchedules; // 예정된 일정 목록
    private int enrolledSubjectsCount;    // 수강 과목 수
    private int activeClassesCount;       // 진행 중인 클래스 수

}
