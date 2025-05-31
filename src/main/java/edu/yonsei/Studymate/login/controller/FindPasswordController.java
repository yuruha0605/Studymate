package edu.yonsei.Studymate.login.controller;


import edu.yonsei.Studymate.login.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class FindPasswordController {
    private final AuthService authService;

    @GetMapping("/study-mate/auth/find-password")
    public String showFindPasswordForm() {
        return "find-password";
    }

    @PostMapping("/study-mate/auth/find-password")
    public String findPassword(@RequestParam String loginId,
                               @RequestParam String studentId,
                               Model model) {
        try {
            String foundPassword = authService.findPasswordByLoginIdAndStudentId(loginId, studentId);
            if (foundPassword != null) {
                model.addAttribute("foundPassword", foundPassword);
            } else {
                model.addAttribute("error", "일치하는 정보가 없습니다.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "비밀번호 찾기 중 오류가 발생했습니다.");
        }
        return "find-password";
    }
}

