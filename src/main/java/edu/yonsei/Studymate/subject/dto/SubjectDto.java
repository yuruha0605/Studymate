package edu.yonsei.Studymate.subject.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)

// 최종적으로 이러한 형태로 다시 user 에게 정보를 보여줄 것이다.
public class SubjectDto {
    private Long id;
    private String subjectName;
    private String professorName;
    private List<StudygroupDto> groupList = List.of();
}
