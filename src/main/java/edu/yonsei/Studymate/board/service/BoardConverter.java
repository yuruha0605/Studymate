package edu.yonsei.Studymate.board.service;

import edu.yonsei.Studymate.board.dto.BoardDto;
import edu.yonsei.Studymate.board.entity.BoardEntity;
import edu.yonsei.Studymate.post.service.PostConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class BoardConverter {

    private final PostConverter postConverter;

    public BoardDto toDto(BoardEntity boardEntity){

        var postList = boardEntity.getPostList()
                .stream()
                .map(postConverter::toDto)
                .collect(Collectors.toList());

        return BoardDto.builder()
                .id(boardEntity.getId())
                .boardName(boardEntity.getBoardName())
                .postList(postList)
                .build()
                ;
    }
}

