package edu.yonsei.Studymate.main.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "schedules")
@Data
//사용자의 일정 목록 (1:N)
public class ScheduleEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private String description;
}
