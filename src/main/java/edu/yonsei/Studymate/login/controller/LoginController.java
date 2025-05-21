package edu.yonsei.Studymate.login.controller;

import edu.yonsei.Studymate.login.repository.UserRepository;
import edu.yonsei.Studymate.login.entity.User;
import jakarta.servlet.http.HttpSession;
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

    // 로그인 페이지
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "아이디나 비밀번호가 올바르지 않습니다.");
        }
        return "a_LogTest";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String handleLogin(@RequestParam String id, @RequestParam String pw, HttpSession session) {
        Optional<User> userOpt = userRepository.findByLoginId(id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(pw)) {
                session.setAttribute("loginUser", user); // 로그인 성공 → 세션에 사용자 저장
                return "redirect:/users/exam";
            }
        }

        return "redirect:/login?error"; // 로그인 실패
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate(); // 세션 삭제
        return "redirect:/login";
    }

    // 로그인 후 이동할 페이지
    @GetMapping("/users/exam")
    public String showDashboardPage(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        model.addAttribute("user", loginUser);
        return "StudyMate_Main";
    }
}
