package edu.yonsei.Studymate.Studygroup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudygroupRequest {

    @NotNull(message = "과목 ID는 null일 수 없습니다.")
    private Long subjectId;

    @NotBlank(message = "스터디 그룹명은 공백일 수 없습니다.")
    private String groupName;

    @NotNull(message = "최대 인원 수는 null일 수 없습니다.")
    private Integer maxMembers;
}
