package edu.yonsei.Studymate.main.entity;

import edu.yonsei.Studymate.login.entity.User;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "assignments")
@Data
//사용자의 과제 목록 (1:N)
public class AssignmentEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private String title;
    @Column(name = "is_active")
    private boolean isActive;
}