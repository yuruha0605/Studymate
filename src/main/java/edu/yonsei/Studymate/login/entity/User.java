package edu.yonsei.Studymate.login.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    // 생성자, getter, setter

    public User() {}

    public User(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }
}
