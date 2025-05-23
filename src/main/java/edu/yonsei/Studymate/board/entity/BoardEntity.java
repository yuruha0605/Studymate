package edu.yonsei.Studymate.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.post.entity.PostEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity(name = "t_board")

public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String boardName;

    @OneToMany(
            mappedBy = "boardEntity"
    )

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "t_studygroup_id")
    private StudygroupEntity studygroupEntity;

    @Builder.Default
    @org.hibernate.annotations.SQLOrder("id")
    private List<PostEntity> postList = List.of();
}
