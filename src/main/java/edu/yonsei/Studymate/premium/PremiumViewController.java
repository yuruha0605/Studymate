package edu.yonsei.Studymate.premium;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("study-mate")
public class PremiumViewController {

    @GetMapping("/paid")
    public String showPaidPage() {
        return "paid";  // paid.html을 렌더링
    }

}
