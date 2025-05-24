package edu.yonsei.Studymate.subject.controller;

import edu.yonsei.Studymate.subject.dto.SubjectDto;
import edu.yonsei.Studymate.subject.dto.SubjectRequest;
import edu.yonsei.Studymate.subject.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectController {

    // 과목 서비스 불러 오기
    private final SubjectService subjectService;

    // 과목 새로 추가
    @PostMapping(path = "/create")
    public SubjectDto addSubject(
            @Valid      // 유효성 검사
            @RequestBody SubjectRequest subjectRequest  // Request 용 변수 만들고
    ){
        return subjectService.create(subjectRequest);   // service.create 호출 하면서 return
    }

    // 특정 과목 검색
    @GetMapping(path = "/article/{id}")
    public SubjectDto view(
            @PathVariable Long id
    ){
        return subjectService.article(id);
    }

    @GetMapping("/api/search")
    public List<SubjectDto> searchSubjects(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "name") String type
    ) {
        return "professor".equalsIgnoreCase(type)
                ? subjectService.searchByProfessor(keyword)
                : subjectService.searchBySubjectName(keyword);
    }
}
