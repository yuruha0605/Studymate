package edu.yonsei.Studymate.Myclass.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyclassViewController {

    @GetMapping("/myclass.html")
    public String showMyclassPage() {
        return "myclass"; // templates/myclass.html을 렌더링
    }
}
