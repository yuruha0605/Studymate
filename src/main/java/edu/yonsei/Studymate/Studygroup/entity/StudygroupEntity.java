package edu.yonsei.Studymate.Studygroup.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.yonsei.Studymate.board.entity.BoardEntity;
import edu.yonsei.Studymate.subject.entity.SubjectEntity;
import jakarta.persistence.*;
import lombok.*;

import javax.security.auth.Subject;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "t_studygroup")
public class StudygroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 과목들과 연결시키기 위해
    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "subject_entity_id")
    private SubjectEntity subjectEntity;

    private String groupName;

    // 각 그룹은 또 각각에 대한 게시판이 있다
    @OneToMany(
            mappedBy = "studygroupEntity"
    )
    @Builder.Default
    @org.hibernate.annotations.SQLOrder("id")
    private List<BoardEntity> boardList = List.of();
}
