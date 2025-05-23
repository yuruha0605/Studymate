package edu.yonsei.Studymate.Studygroup.controller;


import edu.yonsei.Studymate.Studygroup.dto.StudygroupRequest;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.service.StudygroupService;
import edu.yonsei.Studymate.common.Content;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/studygroup") // 이 쪽 URL 로 들어올 경우
public class StudygroupController {

    private final StudygroupService studygroupService;

    // 새로운 스터디 그룹 생성
    @PostMapping(path = "/create")
    public String create(
            @Valid
            @ModelAttribute StudygroupRequest studygroupRequest
    ){
        studygroupService.create(studygroupRequest);
        return "redirect:/studygroup";// + entity.getId();
    }


    // 전체 스터디 그룹 반환
    @GetMapping(path = "/articles")
    public String List (
            @RequestParam int page,
            @RequestParam int size,
            Model model
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"id"));
        Content<List<StudygroupEntity>> content = studygroupService.articles(pageable);
        model.addAttribute("boardArticle", content);

        return "board";

        // 여기까지 진행했음.
    }
}
