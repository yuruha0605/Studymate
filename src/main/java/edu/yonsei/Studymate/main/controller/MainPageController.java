package edu.yonsei.Studymate.main.controller;


//import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.security.CustomUserDetails;
import edu.yonsei.Studymate.main.dto.MainPageDto;
import edu.yonsei.Studymate.main.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/study-mate")
@RequiredArgsConstructor
public class MainPageController {

    private final MainPageService mainPageService;


    @GetMapping("/main")
    public String getMainPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        String loginId = userDetails.getUsername(); // 또는 userDetails.getUser().getLoginId()
        MainPageDto mainPageDto = mainPageService.getMainPageData(loginId); // loginId만 넘김
        model.addAttribute("mainData", mainPageDto);
        return "main";
    }
}