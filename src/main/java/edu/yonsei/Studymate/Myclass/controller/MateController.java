package edu.yonsei.Studymate.Myclass.controller;

import edu.yonsei.Studymate.Myclass.entity.MateEntity;
import edu.yonsei.Studymate.Myclass.service.MateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/myclass")
@RequiredArgsConstructor
public class MateController {

    private final MateService mateService;

    // 스터디 메이트 리스트 조회
    @GetMapping("/mates/{classId}")
    public List<MateEntity> getMates(@PathVariable Long classId) {
        return mateService.getMates(classId);
    }

    // 참여 요청: ?student=홍길동
    @PutMapping("/join/{classId}")
    public void joinClass(
            @PathVariable Long classId,
            @RequestParam String student) {
        mateService.joinClass(classId, student);
    }
}
