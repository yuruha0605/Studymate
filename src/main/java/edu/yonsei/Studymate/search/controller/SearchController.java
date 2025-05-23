package edu.yonsei.Studymate.search.controller;

import edu.yonsei.Studymate.search.entity.SearchEntity;
import edu.yonsei.Studymate.search.repository.SearchRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    private final SearchRepository searchRepository;

    public SearchController(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    /**
     * 검색 API
     * ?keyword=검색어
     * ?type=name 또는 professor (검색 기준)
     */
    @GetMapping("/api/search")
    public List<SearchEntity> searchSubjects(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "name") String type) {

        if ("professor".equalsIgnoreCase(type)) {
            return searchRepository.findByProfessorContainingIgnoreCase(keyword);
        } else {
            return searchRepository.findByNameContainingIgnoreCase(keyword);
        }
    }
}

