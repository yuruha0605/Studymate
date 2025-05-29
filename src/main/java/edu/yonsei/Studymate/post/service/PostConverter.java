package edu.yonsei.Studymate.post.service;

import edu.yonsei.Studymate.post.dto.PostDto;
import edu.yonsei.Studymate.post.dto.PostRequest;
import edu.yonsei.Studymate.post.entity.PostEntity;
import org.springframework.stereotype.Service;

@Service
public class PostConverter {

    public PostDto toDto(PostEntity entity) {
        return PostDto.builder()
                .id(entity.getId())
                .boardId(entity.getBoard() != null ? entity.getBoard().getId() : null)
                .studygroupId(entity.getStudygroup() != null ? entity.getStudygroup().getId() : null)
                .title(entity.getTitle())
                .content(entity.getContent())
                .written(entity.getWritten())
                .build();
    }


    public PostEntity toEntity(PostRequest request) {
        return PostEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }


}


