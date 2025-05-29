package edu.yonsei.Studymate.subject.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)

// 과목 불러 올 때 가지고 와야할 필드들
public class SubjectRequest {

    @NotBlank
    private String subjectName;     // 비어 있으면 안되는 과목명
    private String professorName;   // 비어 있어도 되는 교수명
}
