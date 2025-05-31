package edu.yonsei.Studymate.Studygroup.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.yonsei.Studymate.board.entity.BoardEntity;
import edu.yonsei.Studymate.subject.entity.SubjectEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.security.auth.Subject;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Builder.Default
    private Integer maxMembers = 5;  // 기본 최대 인원

    @Builder.Default
    private Integer currentMembers = 1;  // 방장 포함 1명



    @Enumerated(EnumType.STRING)
    @Builder.Default
    private GroupStatus status = GroupStatus.RECRUITING;

    @CreatedDate
    private LocalDateTime createdAt;


    // 각 그룹은 또 각각에 대한 게시판이 있다
    @OneToMany(mappedBy = "studygroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    @org.hibernate.annotations.SQLOrder("id")
    private List<BoardEntity> boardList = List.of();

    // 스터디 그룹 상태를 나타내는 enum
    public enum GroupStatus {
        RECRUITING,  // 모집 중
        FULL,       // 인원 마감
        CLOSED      // 종료됨
    }

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore
    private List<GroupMember> members = new ArrayList<>();

}
