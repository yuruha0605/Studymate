package edu.yonsei.Studymate.login.controller;

import edu.yonsei.Studymate.common.ApiUrls;
import edu.yonsei.Studymate.login.dto.AuthResponse;
import edu.yonsei.Studymate.login.dto.LoginRequest;
import edu.yonsei.Studymate.login.dto.SignupRequest;
import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/study-mate/auth")
public class AuthViewController {

    private final AuthService authService;

    @Autowired
    public AuthViewController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "로그인 실패! 아이디나 비밀번호를 확인해주세요.");
        }
        model.addAttribute("loginRequest", new LoginRequest());  // 이 부분이 중요합니다
        return "login";
    }


    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginRequest") LoginRequest loginRequest, Model model) {
        try {
            AuthResponse response = authService.login(loginRequest);
            return "redirect:" + ApiUrls.View.MAIN;  // ApiUrls 상수 사용
            // 또는
            // return "redirect:/study-mate/main";   // 직접 경로 지정
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("loginRequest", loginRequest);
            return "login";
        }
    }


    @PostMapping("/signup")
    public String processSignup(@ModelAttribute SignupRequest signupRequest, Model model) {
        try {
            // password가 null이거나 비어있는 경우 처리
            if (signupRequest.getPassword() == null || signupRequest.getPassword().isEmpty()) {
                throw new RuntimeException("비밀번호를 입력해주세요.");
            }

            authService.signup(signupRequest);
            return "redirect:/study-mate/auth/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("learningStyles", Arrays.asList(
                    "개인학습", "그룹스터디", "온라인학습", "오프라인학습",
                    "실습위주", "이론위주", "토론식", "프로젝트기반"
            ));
            model.addAttribute("interests", Arrays.asList(
                    "프로그래밍", "데이터베이스", "웹개발", "인공지능",
                    "네트워크", "보안", "클라우드", "모바일앱"
            ));
            return "signup";
        }
    }


    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        if (!model.containsAttribute("signupRequest")) {
            model.addAttribute("signupRequest", new SignupRequest());
        }
        model.addAttribute("learningStyles", Arrays.asList(
                "개인학습", "그룹스터디", "온라인학습", "오프라인학습",
                "실습위주", "이론위주", "토론식", "프로젝트기반"
        ));
        model.addAttribute("interests", Arrays.asList(
                "프로그래밍", "데이터베이스", "웹개발", "인공지능",
                "네트워크", "보안", "클라우드", "모바일앱"
        ));
        return "signup";
    }

}