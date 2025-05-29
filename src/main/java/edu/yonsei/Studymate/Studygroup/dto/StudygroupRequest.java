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

    @NotNull(message = "과목 ID는 null일 수 없습니다.")
    private Long subjectId;

    @NotBlank(message = "스터디 그룹명은 공백일 수 없습니다.")
    private String groupName;

    @NotNull(message = "최대 인원 수는 null일 수 없습니다.")
    private Integer maxMembers;
}
