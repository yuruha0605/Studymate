package edu.yonsei.Studymate.Studygroup.controller;


import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import edu.yonsei.Studymate.Studygroup.dto.StudygroupRequest;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupRepository;
import edu.yonsei.Studymate.Studygroup.exception.GroupNotFoundException;
import edu.yonsei.Studymate.Studygroup.service.StudygroupConverter;
import edu.yonsei.Studymate.Studygroup.service.StudygroupService;
import edu.yonsei.Studymate.common.ApiUrls;
import edu.yonsei.Studymate.common.Content;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping(ApiUrls.StudyGroup.BASE) //
@Slf4j
public class StudygroupController {

    private final StudygroupService studygroupService;
    private final StudygroupConverter studygroupConverter;
    private final StudygroupRepository studygroupRepository;

    // 새로운 스터디 그룹 생성
    @PostMapping("/create")
    public StudygroupDto create(
            @Valid @RequestBody StudygroupRequest request,
            @RequestParam Long userId
    ) {
        log.info("Received request to create study group. Request: {}, UserId: {}", request, userId);
        return studygroupConverter.toDto(studygroupService.create(request, userId));  // userId 파라미터 사용
    }


    // 전체 스터디 그룹 반환
    @GetMapping(ApiUrls.StudyGroup.LIST)
    public Content<List<StudygroupDto>> list(
            @RequestParam int page,
            @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return studygroupService.getStudyGroupsByStatus(pageable);
    }




    @GetMapping(ApiUrls.StudyGroup.DETAIL)
    public ResponseEntity<?> getGroup(@PathVariable Long groupId) {
        try {
            StudygroupEntity group = studygroupRepository.findById(groupId)
                    .orElseThrow(() -> new GroupNotFoundException("스터디 그룹을 찾을 수 없습니다. ID: " + groupId));
            return ResponseEntity.ok(studygroupConverter.toDto(group));
        } catch (GroupNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(404).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "스터디 그룹 조회 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(error);
        }
    }



    @PostMapping(ApiUrls.StudyGroup.JOIN)
    public StudygroupDto joinGroup(
            @PathVariable Long groupId,
            @RequestParam Long userId
    ) {
        return studygroupService.joinGroup(groupId, userId);
    }


    @DeleteMapping(ApiUrls.StudyGroup.DELETE)
    public void deleteGroup(
            @PathVariable Long groupId,
            @RequestParam Long userId
    ) {
        studygroupService.deleteGroup(groupId, userId);
    }


    @GetMapping(ApiUrls.StudyGroup.SEARCH)
    @CrossOrigin(origins = "*")
    public List<StudygroupDto> searchStudyGroups(
            @RequestParam String keyword,
            @RequestParam String type
    ) {
        return studygroupService.searchStudyGroups(keyword, type);
    }








}
