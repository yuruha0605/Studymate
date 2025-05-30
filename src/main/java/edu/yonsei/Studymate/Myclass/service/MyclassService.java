package edu.yonsei.Studymate.Myclass.service;

import edu.yonsei.Studymate.Myclass.entity.MyclassEntity;
import edu.yonsei.Studymate.Myclass.repository.MyclassRepository;
import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import edu.yonsei.Studymate.Studygroup.entity.*;
import edu.yonsei.Studymate.Studygroup.service.StudygroupConverter;
import edu.yonsei.Studymate.Studygroup.service.StudygroupService;
import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyclassService {

    private final MyclassRepository repository;
    private final GroupMemberRepository groupMemberRepository;
    private final StudygroupService studygroupService;
    private final UserRepository userRepository;
    private final StudygroupConverter studygroupConverter;
    private final MateService mateService;


    public List<MyclassEntity> getAllClasses() {
        return repository.findAll();
    }

    public MyclassEntity joinClass(Long id) {
        MyclassEntity cls = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 수업이 존재하지 않습니다: " + id));
        if (cls.getCurrentParticipants() < cls.getMaxParticipants()) {
            cls.setCurrentParticipants(cls.getCurrentParticipants() + 1);
            return repository.save(cls);
        } else {
            throw new IllegalStateException("정원이 초과되었습니다.");
        }
    }

    // 사용자의 모든 수업(스터디그룹) 조회
    public Map<String, List<StudygroupDto>> getUserClasses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GroupMember> allMemberships = groupMemberRepository.findByUser_Id(userId);
        Map<String, List<StudygroupDto>> result = new HashMap<>();

        // null 체크를 추가하여 필터링
        List<StudygroupDto> myClasses = allMemberships.stream()
                .filter(member -> member.getRole() == MemberRole.LEADER && member.getGroup() != null)
                .map(member -> studygroupConverter.toDto(member.getGroup(), user))
                .collect(Collectors.toList());

        List<StudygroupDto> joinedGroups = allMemberships.stream()
                .filter(member -> member.getRole() == MemberRole.MEMBER && member.getGroup() != null)
                .map(member -> studygroupConverter.toDto(member.getGroup(), user))
                .collect(Collectors.toList());

        result.put("myClasses", myClasses);
        result.put("joinedGroups", joinedGroups);

        return result;
    }



    private StudygroupDto convertToDto(StudygroupEntity entity) {
        return StudygroupDto.builder()
                .id(entity.getId())
                .subjectId(entity.getSubjectEntity().getId())
                .groupName(entity.getGroupName())
                .currentMembers(entity.getCurrentMembers())
                .maxMembers(entity.getMaxMembers())
                .status(entity.getStatus().name())
                .build();
    }

    // 스터디룸 삭제
    public void deleteStudyGroup(Long studyGroupId) {
        // 먼저 모든 메이트 관계 삭제
        mateService.removeAllMatesFromStudy(studyGroupId);

        // 스터디룸 삭제
        repository.deleteById(studyGroupId);
    }


}
