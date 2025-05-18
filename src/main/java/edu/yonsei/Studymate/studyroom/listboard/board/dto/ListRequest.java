package edu.yonsei.Studymate.studyroom.listboard.board.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)

public class ListRequest {
    @NotBlank
    private String ListName;
}
