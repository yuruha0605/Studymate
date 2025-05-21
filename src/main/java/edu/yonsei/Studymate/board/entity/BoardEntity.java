package edu.yonsei.Studymate.board.entity;

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

    @Builder.Default
    @org.hibernate.annotations.SQLOrder("id")
    private List<PostEntity> postList = List.of();
}
