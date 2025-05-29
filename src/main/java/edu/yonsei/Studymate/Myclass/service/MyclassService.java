package edu.yonsei.Studymate.Myclass.service;

import edu.yonsei.Studymate.Myclass.entity.MyclassEntity;
import edu.yonsei.Studymate.Myclass.repository.MyclassRepository;
import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import edu.yonsei.Studymate.Studygroup.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyclassService {

    private final MyclassRepository repository;
    private final StudygroupRepository studygroupRepository;
    private final GroupMemberRepository groupMemberRepository;


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
        List<GroupMember> userGroups = groupMemberRepository.findByUser_Id(userId);

        Map<String, List<StudygroupDto>> result = new HashMap<>();

        // 내가 리더인 그룹
        result.put("myClasses", userGroups.stream()
                .filter(gm -> gm.getRole() == MemberRole.LEADER)
                .map(GroupMember::getGroup)
                .map(this::convertToDto)
                .collect(Collectors.toList()));

        // 내가 참여중인 그룹
        result.put("joinedClasses", userGroups.stream()
                .filter(gm -> gm.getRole() == MemberRole.MEMBER)
                .map(GroupMember::getGroup)
                .map(this::convertToDto)
                .collect(Collectors.toList()));

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

}
