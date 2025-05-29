package edu.yonsei.Studymate.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String loginId;       // 이메일
    private String password;      // 비밀번호
    private String name;          // 이름
    private String studentId;     // 학번
    private String major;         // 전공
    private List<String> selectedLearningStyles;  // 선호하는 학습 방식
    private String studyTime;     // 선호하는 학습 시간
    private List<String> selectedInterests;       // 관심 분야
    private String skillLevel;    // 학습 수준

    // 기본 생성자와 모든 필드를 포함하는 생성자는
    // Lombok의 @AllArgsConstructor와 @NoArgsConstructor가 자동으로 생성
}
