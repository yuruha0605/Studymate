//package edu.yonsei.Studymate.main.service;
//
//import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
//import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
//import edu.yonsei.Studymate.Studygroup.entity.StudygroupRepository;
//import edu.yonsei.Studymate.main.dto.MainPageDto;
//import edu.yonsei.Studymate.main.entity.ScheduleEntity;
//import edu.yonsei.Studymate.main.entity.User;
//import edu.yonsei.Studymate.main.repository.AssignmentRepository;
//import edu.yonsei.Studymate.main.repository.MainUserRepository;
//import edu.yonsei.Studymate.main.repository.ScheduleRepository;
//import edu.yonsei.Studymate.subject.entity.SubjectEntity;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class MainPageService {
//
//    private final MainUserRepository mainUserRepository;
//    private final AssignmentRepository assignmentRepository;
//    private final ScheduleRepository scheduleRepository;
//    private final StudygroupRepository studygroupRepository; // 주입 추가
//    // ✅ loginId를 인자로 받도록 변경
//    public MainPageDto getMainPageData(String loginId) {
//        User user = mainUserRepository.findByLoginId(loginId);
//        if (user == null) {
//            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + loginId);
//        }
//
//        List<String> subjects = user.getSubjects().stream()
//                .map(s -> s.getSubjectName()).toList();
//
//        int activeAssignments = assignmentRepository.countByUserAndIsActiveTrue(user);
//
//        List<String> schedules = scheduleRepository.findByUser(user).stream()
//                .map(ScheduleEntity::getDescription).toList();
//
//        List<StudygroupDto> recommendedGroups = studygroupRepository.findRandom5().stream()
//                .map(group -> {
//                    SubjectEntity subject = group.getSubjectEntity();
//                    String subjectName = subject != null ? subject.getSubjectName() : "알 수 없음";
//
//                    return StudygroupDto.builder()
//                            .id(group.getId())
//                            .groupName(group.getGroupName())
//                            .subjectId(subject != null ? subject.getId() : null)
//                            .subjectName(subjectName)
//                            .currentMembers(group.getCurrentMembers())
//                            .maxMembers(group.getMaxMembers())
//                            .status(group.getStatus() != null ? group.getStatus().name() : "UNKNOWN")
//                            .memberRole("MEMBER")
//                            .build();
//                })
//                .toList();
//
//        return MainPageDto.builder()
//                .loginId(user.getLoginId())
//                .name(user.getName())
//                .studentId(user.getStudentId())
//                .studyTime(user.getStudyTime())
//                .profileImageUrl("https://example.com/img/profile.png")
//                .enrolledSubjects(subjects)
//                .activeAssignmentsCount(activeAssignments)
//                .personalSchedules(schedules)
//                .recommendedStudyGroups(recommendedGroups)
//                .build();
//    }
//}