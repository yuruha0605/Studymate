package edu.yonsei.Studymate.post.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostRequest {

    private Long postId; // 게시글 수정 시 필요
    private Long boardId;
    private Long studygroupId;

    @NotBlank( message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    // 게시글 생성용 생성자
    public PostRequest(Long boardId, String title, String content) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
    }

    // 게시글 수정용 생성자
    public PostRequest(Long postId, Long boardId, String title, String content) {
        this.postId = postId;
        this.boardId = boardId;
        this.title = title;
        this.content = content;
    }

}

