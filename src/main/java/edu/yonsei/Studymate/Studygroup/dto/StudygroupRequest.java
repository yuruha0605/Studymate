package edu.yonsei.Studymate.Studygroup.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudygroupRequest {

    private Long studyGroupId;
    private Long subjectId;
    @NotBlank
    private String groupName;

    @NotBlank
    private String subjectName;

    @Builder.Default
    private Integer maxMembers = 5;


}
