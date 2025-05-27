package edu.yonsei.Studymate.board.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.yonsei.Studymate.post.dto.PostDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)

public class BoardDto {

    private Long id;
    private String boardName;
    private List<PostDto> postList = List.of();
    private Long studygroupId;  // 추가


}

