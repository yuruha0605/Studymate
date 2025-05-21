package edu.yonsei.Studymate.common;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Content<T> {

    private T body;

    private Pagination pagination;
}
