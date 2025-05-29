package edu.yonsei.Studymate.main.service;

import edu.yonsei.Studymate.main.dto.MainPageDto;
import edu.yonsei.Studymate.main.entity.ScheduleEntity;
import edu.yonsei.Studymate.main.entity.User;
import edu.yonsei.Studymate.main.repository.AssignmentRepository;
import edu.yonsei.Studymate.main.repository.MainUserRepository;
import edu.yonsei.Studymate.main.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final MainUserRepository mainUserRepository;
    private final AssignmentRepository assignmentRepository;
    private final ScheduleRepository scheduleRepository;

    // ✅ loginId를 인자로 받도록 변경
    public MainPageDto getMainPageData(String loginId) {
        User user = mainUserRepository.findByLoginId(loginId);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + loginId);
        }

        List<String> subjects = user.getSubjects().stream()
                .map(s -> s.getSubjectName()).toList();

        int activeAssignments = assignmentRepository.countByUserAndIsActiveTrue(user);

        List<String> schedules = scheduleRepository.findByUser(user).stream()
                .map(ScheduleEntity::getDescription).toList();

        return MainPageDto.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .studentId(user.getStudentId())
                .studyTime(user.getStudyTime())
                .profileImageUrl("https://example.com/img/profile.png")
                .enrolledSubjects(subjects)
                .activeAssignmentsCount(activeAssignments)
                .personalSchedules(schedules)
                .build();
    }
}
