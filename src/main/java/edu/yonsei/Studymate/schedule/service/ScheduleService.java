package edu.yonsei.Studymate.schedule.service;

import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupRepository;
import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.schedule.dto.ScheduleDto;
import edu.yonsei.Studymate.schedule.dto.ScheduleRequest;
import edu.yonsei.Studymate.schedule.entity.ScheduleEntity;
import edu.yonsei.Studymate.schedule.entity.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final StudygroupRepository studygroupRepository;

    public ScheduleDto createSchedule(ScheduleRequest request, User currentUser) {
        StudygroupEntity studygroup = studygroupRepository.findById(request.getStudygroupId())
                .orElseThrow(() -> new EntityNotFoundException("스터디 그룹을 찾을 수 없습니다."));

        ScheduleEntity schedule = ScheduleEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .scheduleDateTime(request.getScheduleDateTime())
                .studygroup(studygroup)
                .creator(currentUser)  // 작성자 설정
                .build();

        return convertToDto(scheduleRepository.save(schedule));
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
                .studygroupId(entity.getStudygroup().getId())
                .creatorId(entity.getCreator() != null ? entity.getCreator().getId() : null)  // null 체크 추가
                .createdAt(entity.getCreatedAt())
                .subjectName(entity.getStudygroup().getSubjectEntity().getSubjectName())
                .build();
    }





    public ScheduleDto updateSchedule(Long id, ScheduleRequest request, User currentUser) {
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("일정을 찾을 수 없습니다."));

        // 작성자만 수정 가능하도록 체크
        if (!schedule.getCreator().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("일정 작성자만 수정할 수 있습니다.");
        }

        schedule.setTitle(request.getTitle());
        schedule.setDescription(request.getDescription());
        schedule.setScheduleDateTime(request.getScheduleDateTime());

        return convertToDto(schedule);
    }


    public void deleteSchedule(Long id, User currentUser) {
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("일정을 찾을 수 없습니다."));

        // 작성자만 삭제 가능하도록 체크
        if (!schedule.getCreator().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("일정 작성자만 삭제할 수 있습니다.");
        }

        scheduleRepository.delete(schedule);
    }


    @Transactional(readOnly = true)
    public ScheduleDto getSchedule(Long id) {
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("일정을 찾을 수 없습니다: " + id));
        return convertToDto(schedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleDto> getUpcomingSchedules(Long userId) {
        LocalDateTime now = LocalDateTime.now();

        // 수정된 메서드명으로 호출
        List<StudygroupEntity> userGroups = studygroupRepository.findByUserId(userId);

        return scheduleRepository.findUpcomingSchedulesByStudygroups(userGroups, now)
                .stream()
                .limit(5)
                .map(schedule -> ScheduleDto.builder()
                        .id(schedule.getId())
                        .title(schedule.getTitle())
                        .description(schedule.getDescription())
                        .scheduleDateTime(schedule.getScheduleDateTime())
                        .studygroupId(schedule.getStudygroup().getId())
                        .subjectName(schedule.getStudygroup().getSubjectEntity().getSubjectName())
                        .build())
                .collect(Collectors.toList());
    }







}
