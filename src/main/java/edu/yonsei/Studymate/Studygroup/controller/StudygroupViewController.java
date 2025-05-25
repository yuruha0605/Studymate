package edu.yonsei.Studymate.Studygroup.controller;

import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.service.StudygroupConverter;
import edu.yonsei.Studymate.Studygroup.service.StudygroupService;
import edu.yonsei.Studymate.common.Content;
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
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study-mate")
public class StudygroupViewController {
    private final StudygroupService studygroupService;
    private final StudygroupConverter studygroupConverter;

    @GetMapping("/articles")
    public String list(@RequestParam int page,
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

