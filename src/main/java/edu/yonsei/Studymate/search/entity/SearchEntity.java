package edu.yonsei.Studymate.search.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class SearchEntity {

    // Getter & Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;      // 과목명
    private String professor; // 교수명

    // 기본 생성자
    public SearchEntity() {}

    public SearchEntity(String name, String professor) {
        this.name = name;
        this.professor = professor;
    }

}

