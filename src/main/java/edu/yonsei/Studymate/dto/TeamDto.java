package edu.yonsei.Studymate.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TeamDto {
    private String name;
    private String pm;
    private String member1;
    private String member2;
    private String member3;

}
