package edu.yonsei.Studymate.login.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor  // 기본 생성자
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자
@Builder           // 빌더 패턴 구현
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id",nullable = false, unique = true)
    private String loginId;
    private String password;
    private String name;
    private String studentId;
    private String major;

    @ElementCollection
    private List<String> selectedLearningStyles;

    private String studyTime;

    @ElementCollection
    private List<String> selectedInterests;

    private String skillLevel;

    @Transient
    private String passwordConfirm;

    // loginId와 password만을 위한 생성자는 유지
    @Builder
    public User(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}