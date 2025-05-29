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

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final MainUserRepository mainUserRepository;
    private final AssignmentRepository assignmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final StudygroupRepository studygroupRepository;

    public MainPageDto getMainPageData(String loginId) {
        User user = mainUserRepository.findByLoginId(loginId);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + loginId);
        }

        // 사용자의 과목 목록
        List<String> subjects = user.getSubjects().stream()
                .map(SubjectEntity::getSubjectName)
                .toList();

        // 사용자가 속한 스터디 그룹들의 스케줄 가져오기
        List<StudygroupEntity> userGroups = studygroupRepository.findByMemberUser(user);
        List<String> schedules = userGroups.stream()
                .flatMap(group -> scheduleRepository.findByStudygroupIdOrderByScheduleDateTimeAsc(group.getId())
                        .stream()
                        .map(schedule -> String.format("%s - %s (%s)",
                                schedule.getScheduleDateTime().format(DateTimeFormatter.ofPattern("MM/dd HH:mm")),
                                schedule.getTitle(),
                                group.getGroupName())))
                .limit(5) // 최근 5개만 표시
                .toList();

        return MainPageDto.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .studentId(user.getStudentId())
                .studyTime(user.getStudyTime())
                .profileImageUrl("https://example.com/img/profile.png")
                .enrolledSubjects(subjects)
                .activeSchedules(schedules)
                .build();
    }


}
