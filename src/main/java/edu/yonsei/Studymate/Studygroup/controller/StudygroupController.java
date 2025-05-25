package edu.yonsei.Studymate.Studygroup.controller;


import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import edu.yonsei.Studymate.Studygroup.dto.StudygroupRequest;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.service.StudygroupConverter;
import edu.yonsei.Studymate.Studygroup.service.StudygroupService;
import edu.yonsei.Studymate.common.Content;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study-mate") // 이 쪽 URL 로 들어올 경우
public class StudygroupController {

    private final StudygroupService studygroupService;
    private final StudygroupConverter studygroupConverter;

    // 새로운 스터디 그룹 생성
    @PostMapping(path = "/create")
    public StudygroupDto create(
            @Valid @RequestBody StudygroupRequest studygroupRequest,
            @RequestParam Long userId
    ) {
        StudygroupEntity entity = studygroupService.create(studygroupRequest, userId);
        return studygroupConverter.toDto(entity);
    }




    // 전체 스터디 그룹 반환
    @GetMapping(path = "/articles")
    public Content<List<StudygroupDto>> List(
            @RequestParam int page,
            @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"id"));
        Content<List<StudygroupEntity>> content = studygroupService.articles(pageable);

        List<StudygroupDto> dtoList = content
                .getBody()
                .stream()
                .map(studygroupConverter::toDto)
                .collect(Collectors.toList());

        return Content.<List<StudygroupDto>>builder()
                .body(dtoList)
                .pagination(content.getPagination())
                .build();
    }



    @GetMapping("/search")
    @ResponseBody
    public List<StudygroupDto> searchStudyGroups(
            @RequestParam String keyword,
            @RequestParam String type  // searchType -> type으로 변경
    ) {
        // Service의 결과를 바로 반환
        return studygroupService.searchStudyGroups(keyword, type);
    }

    @PutMapping("/{groupId}")
    public StudygroupDto updateGroup(
            @PathVariable Long groupId,
            @Valid @RequestBody StudygroupRequest request,
            @RequestParam Long userId
    ) {
        return studygroupService.updateGroupInfo(groupId, request, userId);
    }


    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable Long groupId,
            @RequestParam Long userId
    ) {
        studygroupService.deleteGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{groupId}/join")
    public StudygroupDto joinGroup(
            @PathVariable Long groupId,
            @RequestParam Long userId  // userId 파라미터 추가
    ) {
        return studygroupService.joinGroup(groupId, userId);
    }





}
