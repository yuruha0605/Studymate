package edu.yonsei.Studymate.schedule.controller;

import edu.yonsei.Studymate.schedule.dto.ScheduleDto;
import edu.yonsei.Studymate.schedule.dto.ScheduleRequest;
import edu.yonsei.Studymate.schedule.service.ScheduleService;
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
        return ResponseEntity.ok(scheduleService.createSchedule(request));
    }

    @GetMapping("/studygroup/{studygroupId}")
    public ResponseEntity<List<ScheduleDto>> getSchedulesByStudygroup(@PathVariable Long studygroupId) {
        return ResponseEntity.ok(scheduleService.getSchedulesByStudygroup(studygroupId));
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

