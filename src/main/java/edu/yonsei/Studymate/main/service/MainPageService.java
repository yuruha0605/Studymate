package edu.yonsei.Studymate.main.service;

import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupRepository;
import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.main.dto.MainPageDto;
import edu.yonsei.Studymate.schedule.entity.ScheduleRepository;
import edu.yonsei.Studymate.subject.entity.SubjectEntity;


import edu.yonsei.Studymate.main.repository.AssignmentRepository;
import edu.yonsei.Studymate.main.repository.MainUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {
    private final MainUserRepository mainUserRepository;
    private final StudygroupRepository studygroupRepository;
    private final ScheduleRepository scheduleRepository;

    public MainPageDto getMainPageData(String loginId) {
        // 기존 사용자 조회 로직 유지
        User user = mainUserRepository.findByLoginId(loginId);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + loginId);
        }

        // 사용자가 속한 스터디 그룹들 조회
        List<StudygroupEntity> userGroups = studygroupRepository.findByMemberUser(user);

        // 수강 과목 수 계산 (중복 제거)
        long subjectCount = userGroups.stream()
                .map(group -> group.getSubjectEntity().getId())
                .distinct()
                .count();

        // 현재 시점 이후의 가까운 일정들 가져오기
        List<String> upcomingSchedules = scheduleRepository
                .findUpcomingSchedulesByStudygroups(userGroups, LocalDateTime.now())
                .stream()
                .limit(5)
                .map(schedule -> String.format("%s - %s (%s)",
                        schedule.getScheduleDateTime().format(DateTimeFormatter.ofPattern("MM/dd HH:mm")),
                        schedule.getTitle(),
                        schedule.getStudygroup().getSubjectEntity().getSubjectName()))
                .toList();

        return MainPageDto.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .studentId(user.getStudentId())
                .profileImageUrl("https://example.com/img/profile.png")
                .enrolledSubjectsCount((int) subjectCount)
                .activeClassesCount(userGroups.size())
                .activeSchedules(upcomingSchedules)  // 일정 정보 추가
                .build();
    }
}

