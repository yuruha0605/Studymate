package edu.yonsei.Studymate.Myclass.controller;

import edu.yonsei.Studymate.Myclass.service.MyclassService;
import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import edu.yonsei.Studymate.Studygroup.service.StudygroupService;
import edu.yonsei.Studymate.login.security.CustomUserDetails;
import edu.yonsei.Studymate.login.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study-mate")
public class MyclassViewController {
    private final StudygroupService studygroupService;
    private final MyclassService myclassService;


//    @GetMapping("/myclass")
//    public String showMyclassPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
//        // CustomUserDetails를 사용하여 userId 추출
//        Long userId = ((CustomUserDetails) userDetails).getId();
//        // 또는 userDetails.getUsername()을 사용하여 사용자 정보 조회 후 ID 추출
//
//        Map<String, List<StudygroupDto>> classes = studygroupService.getUserStudyGroups(userId);
//        model.addAttribute("myClasses", classes.get("myGroups"));
//        model.addAttribute("joinedClasses", classes.get("joinedGroups"));
//        return "myclass";
//    }

    @GetMapping("/myclass")
    public String myClass(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long userId = userDetails.getId();
        Map<String, List<StudygroupDto>> userClasses = myclassService.getUserClasses(userId);
        model.addAttribute("userClasses", userClasses);
        return "myclass";  // myclass.html 템플릿을 사용
    }

}

