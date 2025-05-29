package edu.yonsei.Studymate.common;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
// HTTP Response에 본문 정보와 page 정보를 함께 전송 하기 위한 class
public class Content<T> {
    private T body;
    private Pagination pagination;
}
