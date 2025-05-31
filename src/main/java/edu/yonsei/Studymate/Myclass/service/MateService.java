package edu.yonsei.Studymate.Myclass.service;

import edu.yonsei.Studymate.Myclass.dto.MateDto;
import edu.yonsei.Studymate.Myclass.entity.MateEntity;
import edu.yonsei.Studymate.Myclass.entity.MyclassEntity;
import edu.yonsei.Studymate.Myclass.entity.StudentEntity;
import edu.yonsei.Studymate.Myclass.repository.MateRepository;
import edu.yonsei.Studymate.Myclass.repository.MyclassRepository;
import edu.yonsei.Studymate.Myclass.repository.StudentRepository;
import edu.yonsei.Studymate.Studygroup.entity.GroupMember;
import edu.yonsei.Studymate.Studygroup.entity.GroupMemberRepository;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupRepository;
import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MateService {

    private final MateRepository mateRepository;
    private final StudygroupRepository studygroupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;



    public List<MateEntity> getMates(Long studygroupId) {
        StudygroupEntity studygroup = studygroupRepository.findById(studygroupId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스터디그룹이 없습니다."));
        return mateRepository.findByStudygroup(studygroup);
    }

    @Transactional
    public void updateUserActivity(Long userId) {
        List<MateEntity> userMates = mateRepository.findByUser_Id(userId);
        LocalDateTime now = LocalDateTime.now();

        if (userMates.isEmpty()) {
            log.warn("No mate entries found for user ID: {}", userId);
            return;
        }

        for (MateEntity mate : userMates) {
            mate.setLastActiveTime(now);
            mate.setOnline(true);
            mateRepository.save(mate);
        }
    }

    @Transactional(readOnly = true)
    public List<MateDto> getTopMates(Long userId, int limit) {
        List<MateEntity> mates = mateRepository.findTopMatesByUserId(userId, PageRequest.of(0, limit));

        return mates.stream()
                .map(mate -> MateDto.builder()
                        .id(mate.getId())
                        .studentName(mate.getUser().getName())  // User의 name 사용
                        .lastActiveTime(mate.getLastActiveTime())
                        .build())
                .collect(Collectors.toList());
    }

    // 스터디룸에서 특정 멤버 제거
    public void removeMateFromStudy(Long studyGroupId, Long userId) {
        StudygroupEntity studygroup = studygroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new IllegalArgumentException("스터디룸을 찾을 수 없습니다."));

        List<MateEntity> mates = mateRepository.findByStudygroup(studygroup);
        mates.stream()
                .filter(mate -> mate.getUser().getId().equals(userId))
                .forEach(mateRepository::delete);
    }

    // 스터디룸 삭제 시 모든 메이트 제거
    public void removeAllMatesFromStudy(Long studyGroupId) {
        StudygroupEntity studygroup = studygroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new IllegalArgumentException("스터디룸을 찾을 수 없습니다."));

        List<MateEntity> mates = mateRepository.findByStudygroup(studygroup);
        mateRepository.deleteAll(mates);
    }

    @Transactional(readOnly = true)
    public List<GroupMember> getActiveStudyMates(Long userId, int limit) {
        // 사용자 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 해당 사용자가 속한 그룹의 모든 멤버를 조회
        List<GroupMember> userGroups = groupMemberRepository.findByUser_Id(userId);

        // 사용자가 속한 그룹들의 모든 멤버를 수집
        Set<GroupMember> allGroupMembers = new HashSet<>();

        for (GroupMember membership : userGroups) {
            // 각 그룹의 모든 멤버를 가져옴
            List<GroupMember> groupMembers = groupMemberRepository.findByGroup(membership.getGroup());
            // 자신을 제외한 멤버들만 추가
            groupMembers.stream()
                    .filter(member -> !member.getUser().getId().equals(userId))
                    .forEach(allGroupMembers::add);
        }

        // 최대 limit 수만큼 반환
        return allGroupMembers.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

}
