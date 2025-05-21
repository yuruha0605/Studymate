package edu.yonsei.Studymate.search.controller;

import edu.yonsei.Studymate.search.entity.SubjectEntity;
import edu.yonsei.Studymate.search.repository.SubjectRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubjectController {

    private final SubjectRepository subjectRepository;

    public SubjectController(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    /**
     * 검색 API
     * ?keyword=검색어
     * ?type=name 또는 professor (검색 기준)
     */
    @GetMapping("/api/search")
    public List<SubjectEntity> searchSubjects(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "name") String type) {

        if ("professor".equalsIgnoreCase(type)) {
            return subjectRepository.findByProfessorContainingIgnoreCase(keyword);
        } else {
            return subjectRepository.findByNameContainingIgnoreCase(keyword);
        }
    }
}

