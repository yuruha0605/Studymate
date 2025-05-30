package edu.yonsei.Studymate.Myclass.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)

public class MateDto {
    private Long id;
    private String studentName;
    //private boolean isOnline;
    private LocalDateTime lastActiveTime;
}

