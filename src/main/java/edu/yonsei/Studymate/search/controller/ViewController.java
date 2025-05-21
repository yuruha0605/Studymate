package edu.yonsei.Studymate.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/search")
    public String showSearchPage() {
        return "search";  // templates/search.html 보여줌
    }
}