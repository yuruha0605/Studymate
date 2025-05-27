package edu.yonsei.Studymate.login.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private String token;  // JWT를 사용한다면
    private String username;
    private String message;
}

