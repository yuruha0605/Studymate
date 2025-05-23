package edu.yonsei.Studymate.login.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;
    private String password;
    //private String email;
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

    //생성자
    public User() {}

    public User(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }



    //passwordConfirm의 getter, setter
    public String getPasswordConfirm(){
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }




}
