package edu.yonsei.Studymate.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.yonsei.Studymate.board.entity.BoardEntity;
import edu.yonsei.Studymate.reply.entity.ReplyEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity(name = "t_bbs2025")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "t_board_id")
    private BoardEntity boardEntity;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime written;

    @OneToMany(
            mappedBy = "postEntity"
    )
    @Builder.Default
    @org.hibernate.annotations.SQLOrder("id")
    private List<ReplyEntity> replyList = List.of();
}
