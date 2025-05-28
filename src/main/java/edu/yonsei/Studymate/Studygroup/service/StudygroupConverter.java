package edu.yonsei.Studymate.Studygroup.service;

import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import edu.yonsei.Studymate.Studygroup.entity.GroupMember;
import edu.yonsei.Studymate.Studygroup.entity.GroupMemberRepository;
import edu.yonsei.Studymate.Studygroup.entity.MemberRole;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.board.service.BoardConverter;
import edu.yonsei.Studymate.login.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudygroupConverter {

    private final GroupMemberRepository groupMemberRepository;

    public StudygroupDto toDto(StudygroupEntity studygroupEntity, User currentUser) {
        MemberRole role = null;
        if (currentUser != null) {
            Optional<GroupMember> member = groupMemberRepository.findByGroupAndUser(studygroupEntity, currentUser);
            if (member.isPresent()) {
                role = member.get().getRole();
            }
        }

        return StudygroupDto.builder()
                .id(studygroupEntity.getId())
                .subjectId(studygroupEntity.getSubjectEntity().getId())
                .groupName(studygroupEntity.getGroupName())
                .subjectName(studygroupEntity.getSubjectEntity().getSubjectName())
                .currentMembers(studygroupEntity.getCurrentMembers())
                .maxMembers(studygroupEntity.getMaxMembers())
                .status(studygroupEntity.getStatus() != null ?
                        studygroupEntity.getStatus().name() :
                        StudygroupEntity.GroupStatus.RECRUITING.name())
                .memberRole(role != null ? role.name() : null)
                .build();
    }

    // 새로운 오버로드된 메서드
    public StudygroupDto toDto(StudygroupEntity studygroupEntity) {
        return StudygroupDto.builder()
                .id(studygroupEntity.getId())
                .subjectId(studygroupEntity.getSubjectEntity().getId())
                .groupName(studygroupEntity.getGroupName())
                .subjectName(studygroupEntity.getSubjectEntity().getSubjectName())
                .currentMembers(studygroupEntity.getCurrentMembers())
                .maxMembers(studygroupEntity.getMaxMembers())
                .status(studygroupEntity.getStatus() != null ?
                        studygroupEntity.getStatus().name() :
                        StudygroupEntity.GroupStatus.RECRUITING.name())
                .build();
    }





}
