package edu.yonsei.Studymate.subject.controller;

import edu.yonsei.Studymate.common.ApiUrls;
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
@RequestMapping(ApiUrls.Subject.BASE) // "/api/study-mate/subjects"
@CrossOrigin(origins = "*")


public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/list")
    public List<SubjectDto> getSubjectList() {
        return subjectService.getAllSubjects();
    }



    // 과목 새로 추가
    @PostMapping(ApiUrls.Subject.CREATE)
    public SubjectDto addSubject(@Valid @RequestBody SubjectRequest subjectRequest) {
        return subjectService.create(subjectRequest);
    }




    @GetMapping(ApiUrls.Subject.DETAIL)
    public SubjectDto view(@PathVariable Long subjectId) {
        return subjectService.article(subjectId);
    }


    @GetMapping(ApiUrls.Subject.SEARCH)
    public List<SubjectDto> searchSubjects(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "name") String type
    ) {
        return "professor".equalsIgnoreCase(type)
                ? subjectService.searchByProfessor(keyword)
                : subjectService.searchBySubjectName(keyword);
    }

}
