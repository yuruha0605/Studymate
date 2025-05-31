package edu.yonsei.Studymate.Myclass.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.login.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JsonIgnore
    private StudygroupEntity studygroup;  // MyclassEntity 대신 StudygroupEntity 사용

    @ManyToOne
    @JsonIgnore
    private User user;  // StudentEntity 대신 User 사용

    private boolean isOnline;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime lastActiveTime = LocalDateTime.now();
}

