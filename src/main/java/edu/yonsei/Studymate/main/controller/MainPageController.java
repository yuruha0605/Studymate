package edu.yonsei.Studymate.main.controller;


//import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupRepository;
import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.security.CustomUserDetails;
import edu.yonsei.Studymate.main.dto.MainPageDto;
import edu.yonsei.Studymate.main.service.MainPageService;
import edu.yonsei.Studymate.subject.entity.SubjectEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/study-mate")
@RequiredArgsConstructor
public class MainPageController {
    private final MainPageService mainPageService;
    private final StudygroupRepository studygroupRepository;

    @GetMapping("/main")
    public String getMainPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        // userDetails가 null인 경우 처리
        if (userDetails == null) {
            // 로그인 페이지로 리다이렉트
            return "redirect:/study-mate/login";
        }

        String loginId = userDetails.getUsername();
        MainPageDto mainPageDto = mainPageService.getMainPageData(loginId);

        // 추천 스터디 그룹 추가
        List<StudygroupDto> recommendedGroups = studygroupRepository.findRandom6().stream()
                .map(group -> {
                    SubjectEntity subject = group.getSubjectEntity();
                    return StudygroupDto.builder()
                            .id(group.getId())
                            .groupName(group.getGroupName())
                            .subjectName(subject != null ? subject.getSubjectName() : "알 수 없음")
                            .currentMembers(group.getCurrentMembers())
                            .maxMembers(group.getMaxMembers())
                            .status(group.getStatus() != null ? group.getStatus().name() : "UNKNOWN")
                            .build();
                })
                .toList();

        model.addAttribute("recommendedGroups", recommendedGroups);
        model.addAttribute("mainData", mainPageDto);
        return "main";
    }
}
