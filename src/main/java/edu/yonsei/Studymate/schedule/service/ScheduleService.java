package edu.yonsei.Studymate.schedule.service;

import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupRepository;
import edu.yonsei.Studymate.schedule.dto.ScheduleDto;
import edu.yonsei.Studymate.schedule.dto.ScheduleRequest;
import edu.yonsei.Studymate.schedule.entity.ScheduleEntity;
import edu.yonsei.Studymate.schedule.entity.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final StudygroupRepository studygroupRepository;

    public ScheduleDto createSchedule(ScheduleRequest request) {
        StudygroupEntity studygroup = studygroupRepository.findById(request.getStudygroupId())
                .orElseThrow(() -> new EntityNotFoundException("스터디 그룹을 찾을 수 없습니다."));

        ScheduleEntity schedule = ScheduleEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .scheduleDateTime(request.getScheduleDateTime())
                .studygroup(studygroup)
                .build();

        ScheduleEntity savedSchedule = scheduleRepository.save(schedule);
        return convertToDto(savedSchedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleDto> getSchedulesByStudygroup(Long studygroupId) {
        // 스터디 그룹 존재 여부 확인
        studygroupRepository.findById(studygroupId)
                .orElseThrow(() -> new EntityNotFoundException("스터디 그룹을 찾을 수 없습니다."));

        return scheduleRepository.findByStudygroupIdOrderByScheduleDateTimeAsc(studygroupId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private ScheduleDto convertToDto(ScheduleEntity entity) {
        return ScheduleDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .scheduleDateTime(entity.getScheduleDateTime())
                .studygroupId(entity.getStudygroup().getId())  // LAZY 로딩 문제 발생 지점
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public ScheduleDto updateSchedule(Long id, ScheduleRequest request) {
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("일정을 찾을 수 없습니다."));

        schedule.setTitle(request.getTitle());
        schedule.setDescription(request.getDescription());
        schedule.setScheduleDateTime(request.getScheduleDateTime());

        return convertToDto(schedule);
    }

    public void deleteSchedule(Long id) {
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("일정을 찾을 수 없습니다."));
        scheduleRepository.delete(schedule);
    }

    @Transactional(readOnly = true)
    public ScheduleDto getSchedule(Long id) {
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("일정을 찾을 수 없습니다: " + id));
        return convertToDto(schedule);
    }


}
