package edu.yonsei.Studymate.Studygroup.controller;

import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.service.StudygroupConverter;
import edu.yonsei.Studymate.Studygroup.service.StudygroupService;
import edu.yonsei.Studymate.common.ApiUrls;
import edu.yonsei.Studymate.common.Content;
import edu.yonsei.Studymate.subject.dto.SubjectDto;
import edu.yonsei.Studymate.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class StudygroupViewController {
    private final StudygroupService studygroupService;
    private final StudygroupConverter studygroupConverter;
    private final SubjectService subjectService;

    @GetMapping(ApiUrls.View.MY_CLASS)
    public String myClassPage(Model model, @RequestParam Long userId) {
        // 기존 코드
        Map<String, List<StudygroupDto>> userGroups = studygroupService.getUserStudyGroups(userId);
        model.addAttribute("myGroups", userGroups.get("myGroups"));
        model.addAttribute("joinedGroups", userGroups.get("joinedGroups"));

        // 과목 리스트 추가
        List<SubjectDto> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);

        return "myclass";
    }



    @GetMapping(ApiUrls.View.STUDY_GROUPS)
    public String listStudyGroups(@RequestParam int page,
                       @RequestParam int size,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"id"));
        Content<List<StudygroupEntity>> content = studygroupService.articles(pageable);

        List<StudygroupDto> dtoList = content.getBody().stream()
                .map(studygroupConverter::toDto)
                .collect(Collectors.toList());

        var dtoContent = Content.<List<StudygroupDto>>builder()
                .body(dtoList)
                .pagination(content.getPagination())
                .build();

        model.addAttribute("boardArticle", dtoContent);
        return "board";
    }
}

