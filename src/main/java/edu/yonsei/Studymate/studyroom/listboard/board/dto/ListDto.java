package edu.yonsei.Studymate.studyroom.listboard.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)

public class ListDto {
    private Long id;
    private String ListName;
    private list<PostDto> postList = List.of();
}
