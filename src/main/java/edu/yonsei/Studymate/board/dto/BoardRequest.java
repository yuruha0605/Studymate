package edu.yonsei.Studymate.board.dto;

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
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class) // 직렬화 과정의 naming rule 이다.

public class BoardRequest {

    // 해당 field에 대한 유효성 검사
    @NotBlank
    private String boardName;

}
