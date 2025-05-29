package edu.yonsei.Studymate.main.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "MainSubjectEntity") // 중복 해결용 엔터티 이름 설정
@Table(name = "subject_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "subject_name", nullable = false)
//    private String subjectName;
    @Column(name = "subject_name")
    private String subjectName;
    @Column(name = "professor_name")
    private String professorName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "subjects")
    private List<User> users;
}
