package edu.yonsei.Studymate.post.service;

import edu.yonsei.Studymate.post.dto.PostDto;
import edu.yonsei.Studymate.post.entity.PostEntity;
import org.springframework.stereotype.Service;

@Service
public class PostConverter {

    public PostDto toDto(PostEntity postEntity){
        return PostDto.builder()
                .id(postEntity.getId())
                .boardId(postEntity.getBoardEntity().getId())
                .subject(postEntity.getSubject())
                .content(postEntity.getContent())
                .written(postEntity.getWritten())
                .build()
                ;
    }
}


