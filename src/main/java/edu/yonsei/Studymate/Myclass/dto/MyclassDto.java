package edu.yonsei.Studymate.Myclass.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyclassDto {
    private String name;
    private int maxParticipants;
    private int currentParticipants;
}
