package edu.yonsei.Studymate.login.controller;

import edu.yonsei.Studymate.login.repository.UserRepository;
import edu.yonsei.Studymate.login.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "아이디나 비밀번호가 올바르지 않습니다.");
        }
        return "a_LogTest";
    }

    // 리디렉션 테스트
    @GetMapping("/users/exam")
    public String showDashboardPage() {
        return "StudyMate_Main";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String id, @RequestParam String pw) {
        Optional<User> userOpt = userRepository.findByLoginId(id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(pw)) {
                return "redirect:/users/exam";
            }
        }

        return "redirect:/login?error";
    }
}