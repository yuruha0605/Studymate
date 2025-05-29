package edu.yonsei.Studymate.schedule.controller;

import edu.yonsei.Studymate.login.dto.ErrorResponse;
import edu.yonsei.Studymate.schedule.dto.ScheduleDto;
import edu.yonsei.Studymate.schedule.dto.ScheduleRequest;
import edu.yonsei.Studymate.schedule.service.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-mate/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/create")
    public ResponseEntity<ScheduleDto> createSchedule(@RequestBody @Valid ScheduleRequest request) {
        System.out.println("Received schedule request: " + request); // 디버깅용
        return ResponseEntity.ok(scheduleService.createSchedule(request));
    }


    @GetMapping("/studygroup/{studygroupId}")
    public ResponseEntity<?> getSchedulesByStudygroup(@PathVariable Long studygroupId) {
        try {
            System.out.println("Fetching schedules for studygroup: " + studygroupId); // 디버깅용
            List<ScheduleDto> schedules = scheduleService.getSchedulesByStudygroup(studygroupId);
            return ResponseEntity.ok(schedules);
        } catch (EntityNotFoundException e) {
            System.err.println("Entity not found: " + e.getMessage());
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e);
            e.printStackTrace(); // 스택 트레이스 출력
            return ResponseEntity.status(500)
                    .body(new ErrorResponse("일정을 불러오는 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDto> getSchedule(@PathVariable Long id) {
        try {
            ScheduleDto schedule = scheduleService.getSchedule(id);
            return ResponseEntity.ok(schedule);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody @Valid ScheduleRequest request) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }


}

