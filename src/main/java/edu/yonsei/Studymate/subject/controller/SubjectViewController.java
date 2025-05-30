package edu.yonsei.Studymate.subject.controller;

import edu.yonsei.Studymate.login.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/study-mate")
public class SubjectViewController {

    @GetMapping("/search-page")
    public String showSearchPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("userId", userDetails.getId());
        }
        return "search";
    }

}

