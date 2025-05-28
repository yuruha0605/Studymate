package edu.yonsei.Studymate.schedule.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduleRequest {
    @NotBlank(message = "일정 제목을 입력해주세요.")
    private String title;

    private String description;

    @NotNull(message = "일정 날짜와 시간을 입력해주세요.")
    private LocalDateTime scheduleDateTime;

    private Long studygroupId;
}

