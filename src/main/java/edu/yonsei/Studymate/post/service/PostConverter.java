package edu.yonsei.Studymate.post.service;

import edu.yonsei.Studymate.post.dto.PostDto;
import edu.yonsei.Studymate.post.dto.PostRequest;
import edu.yonsei.Studymate.post.entity.PostEntity;
import org.springframework.stereotype.Service;

@Service
public class PostConverter {

    public PostDto toDto(PostEntity postEntity) {
        String authorEmail = "작성자 정보 없음";
        Long authorId = null;

        if (postEntity.getAuthor() != null) {
            // 작성자의 loginId를 사용 (이메일)
            authorEmail = postEntity.getAuthor().getLoginId();
            authorId = postEntity.getAuthor().getId();
        }

        return PostDto.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .written(postEntity.getWritten())
                .authorEmail(authorEmail)  // loginId를 authorEmail로 사용
                .authorId(authorId)
                .boardId(postEntity.getBoard() != null ? postEntity.getBoard().getId() : null)
                .build();
    }

    public PostEntity toEntity(PostRequest request) {
        return PostEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(request.getAuthor())
                .build();
    }
}




