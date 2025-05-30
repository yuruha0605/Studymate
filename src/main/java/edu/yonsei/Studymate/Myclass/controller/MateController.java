package edu.yonsei.Studymate.Myclass.controller;

import edu.yonsei.Studymate.Myclass.entity.MateEntity;
import edu.yonsei.Studymate.Myclass.service.MateService;
import edu.yonsei.Studymate.Studygroup.service.StudygroupService;
import edu.yonsei.Studymate.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-mate/mate")  // 경로 수정
@RequiredArgsConstructor
public class MateController {

    private final MateService mateService;
    private final UserRepository userRepository;
    private final StudygroupService studygroupService;

    // 스터디 메이트 리스트 조회
    @GetMapping("/mates/{studyGroupId}")
    public List<MateEntity> getMates(@PathVariable Long studyGroupId) {
        return mateService.getMates(studyGroupId);
    }

    // 참여 요청 - userId를 사용하도록 변경
    @PutMapping("/join/{studyGroupId}")
    public ResponseEntity<Void> joinClass(
            @PathVariable Long studyGroupId,
            @RequestParam Long userId) {
        try {
            // StudygroupService의 joinGroup 메서드를 사용
            studygroupService.joinGroup(studyGroupId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/activity/{userId}")
    public ResponseEntity<Void> updateActivity(@PathVariable Long userId) {
        try {
            mateService.updateUserActivity(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
