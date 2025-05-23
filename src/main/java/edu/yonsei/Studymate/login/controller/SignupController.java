package edu.yonsei.Studymate.login.controller;

import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SignupController {

    private final UserRepository userRepository;

    public SignupController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/study-mate/signup")
    public String showSignupForm(Model model) {

        // User 객체 생성하여 모델 추가
        model.addAttribute("user", new User());

        // 학습 스타일 옵션
        List<String> learningStyles = Arrays.asList(
                "개인 학습", "그룹 스터디", "온라인 강의", "실습 위주", "이론 중심"
        );
        model.addAttribute("learningStyles", learningStyles);

        // 관심 분야 옵션
        List<String> interests = Arrays.asList(
                "프로그래밍", "데이터베이스", "인공지능", "웹개발",
                "모바일앱", "네트워크", "보안"
        );
        model.addAttribute("interests", interests);

        return "signup"; // signup.html을 사용
    }

    @PostMapping("/study-mate/signup")
    public String handleSignup(
            @ModelAttribute User user,
            RedirectAttributes redirectAttributes
    ) {

        // 중복 아이디 체크
        if (userRepository.findByLoginId(user.getLoginId()).isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미 사용중인 아이디입니다.");
            return "redirect:/study-mate/signup";
        }

        // 새 사용자 생성
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다. 로그인하세요.");
        return "redirect:/study-mate/login";
    }

    @GetMapping("/study-mate/check-email")
    @ResponseBody  // JSON 응답을 위해 필요
    public Map<String, Boolean> checkEmailDuplicate(@RequestParam String email) {
        boolean isDuplicate = userRepository.findByLoginId(email).isPresent();
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return response;
    }


}