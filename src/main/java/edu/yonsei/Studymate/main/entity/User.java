package edu.yonsei.Studymate.main.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "MainUser") // 	JPA 내부의 식별 이름 (중복 안 됨)
@Table(name = "user")      // 실제 DB 테이블명은 'user'
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    private String password;
    private String major;
    private String name;
    private String skillLevel;
    private String studentId;
    private String studyTime;

    @ManyToMany(fetch = FetchType.LAZY) // 이걸 명시적으로 쓰고
    @JoinTable(
            name = "user_subject",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<SubjectEntity> subjects;

    @OneToMany(mappedBy = "user")
    private List<AssignmentEntity> assignments;

    @OneToMany(mappedBy = "user")
    private List<ScheduleEntity> schedules;
}