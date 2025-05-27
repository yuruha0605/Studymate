package edu.yonsei.Studymate.main.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/study-mate")
public class MainViewController {

    @GetMapping("/main")
    public String showMainPage() {
        return "main";  // main.html 템플릿을 반환
    }
}


