package edu.yonsei.Studymate.login.controller;

import edu.yonsei.Studymate.login.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class FindIdController {

    private final AuthService authService;

    @GetMapping("/study-mate/auth/find-id")
    public String showFindIdForm() {
        return "find-id";
    }

    @PostMapping("/study-mate/auth/find-id")
    public String findId(@RequestParam String studentId,
                         @RequestParam String name,
                         Model model) {
        try {
            String foundId = authService.findIdByStudentIdAndName(studentId, name);
            if (foundId != null) {
                model.addAttribute("foundId", foundId);
            } else {
                model.addAttribute("error", "일치하는 정보가 없습니다.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "아이디 찾기 중 오류가 발생했습니다.");
        }
        return "find-id";
    }

}
