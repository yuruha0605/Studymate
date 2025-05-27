package edu.yonsei.Studymate.login.controller;

import edu.yonsei.Studymate.common.ApiUrls;
import edu.yonsei.Studymate.login.dto.AuthResponse;
import edu.yonsei.Studymate.login.dto.ErrorResponse;
import edu.yonsei.Studymate.login.dto.LoginRequest;
import edu.yonsei.Studymate.login.dto.SignupRequest;
import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.repository.UserRepository;
import edu.yonsei.Studymate.login.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(ApiUrls.Auth.BASE)
@RequiredArgsConstructor
public class AuthApiController {
    private final AuthService authService;
    private final UserRepository userRepository;


    // 이메일 중복 확인 엔드포인트 추가
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean isDuplicate = userRepository.existsByLoginId(email);
        return ResponseEntity.ok(Map.of("isDuplicate", isDuplicate));
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> processSignup(@RequestBody SignupRequest signupRequest) {
        try {
            authService.signup(signupRequest);
            return ResponseEntity.ok(Map.of(
                    "message", "회원가입이 성공적으로 완료되었습니다."
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }



}
