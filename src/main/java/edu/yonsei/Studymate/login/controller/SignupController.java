package edu.yonsei.Studymate.login.controller;

import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignupController {

    private final UserRepository userRepository;

    public SignupController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //HTML 이름 반환
    @GetMapping("/study-mate/signup")
    public String showSignupForm() {
        return "signup";
    }

    @PostMapping("/study-mate/signup")
    public String handleSignup(
            @RequestParam String loginId,
            @RequestParam String password,
//            @RequestParam String name,
            RedirectAttributes redirectAttributes) {

        // 중복 아이디 체크
        if (userRepository.findByLoginId(loginId).isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미 사용중인 아이디입니다.");
            return "redirect:/study-mate/signup";
        }

        // 회원정보 저장
        User newUser = new User();
        newUser.setLoginId(loginId);
        newUser.setPassword(password); // 실무에서는 암호화 필수
//        newUser.setName(name);

        userRepository.save(newUser);

        redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다. 로그인하세요.");
        return "redirect:/study-mate/login";
    }
}

