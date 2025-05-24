package edu.yonsei.Studymate.subject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/subject")
public class SubjectViewController {

    @GetMapping("/search-page")  // URL 변경
    public String showSearchPage() {
        return "search";
    }
}

