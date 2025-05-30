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

    private Long userid;
    private Long studyGroupId;
    private Long subjectId;
    @NotBlank(message = "스터디 그룹 이름을 입력해주세요")
    private String groupName;


    private String subjectName;


    @Builder.Default
    private Integer maxMembers = 5;


}
