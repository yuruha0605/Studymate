package edu.yonsei.Studymate.Studygroup.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudygroupDto {

    private Long id;
    private Long subjectId;
    private String groupName;
    private String subjectName;
    private Integer currentMembers;
    private Integer maxMembers;
    private String status;

}
