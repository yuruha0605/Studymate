package edu.yonsei.Studymate.subject.entity;


import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity(name = "subject_entity") // table name
public class SubjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 알아서 증가
    private Long id;

    private String subjectName;
    private String professorName;

    // subject -> study group 이랑 1-N 관계
    @OneToMany(
            mappedBy = "subjectEntity"
    )

    @Builder.Default
    @org.hibernate.annotations.SQLOrder("id")
    private List<StudygroupEntity> groupList = List.of();   // List 로 만든 group 을 반환
}
