package edu.yonsei.Studymate.material.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "materials")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private Long studygroupId;

    @Column(nullable = false)
    private LocalDateTime uploadDateTime;
}

