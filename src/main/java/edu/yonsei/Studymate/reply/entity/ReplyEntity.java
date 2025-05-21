package edu.yonsei.Studymate.reply.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.yonsei.Studymate.post.entity.PostEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name = "t_reply2025")
public class ReplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "t_post_id")
    private PostEntity postEntity;

    @Column(columnDefinition = "TEXT")
    private String content;
}