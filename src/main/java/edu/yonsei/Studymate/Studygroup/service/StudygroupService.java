package edu.yonsei.Studymate.Studygroup.service;


import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import edu.yonsei.Studymate.Studygroup.dto.StudygroupRequest;
import edu.yonsei.Studymate.Studygroup.entity.*;
import edu.yonsei.Studymate.Studygroup.exception.*;
import edu.yonsei.Studymate.common.Content;
import edu.yonsei.Studymate.common.Pagination;
import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.repository.UserRepository;
import edu.yonsei.Studymate.post.entity.PostEntity;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.data.domain.Pageable;
import edu.yonsei.Studymate.subject.entity.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;



@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudygroupService {

    private final StudygroupRepository studygroupRepository;
    private final SubjectRepository subjectRepository;
    private final StudygroupConverter studygroupConverter;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;


    // study group 을 새로 만들었을 때
    @Transactional
    public StudygroupEntity create(StudygroupRequest request, Long userId) {  // userId 파라미터 추가
        var subjectEntity = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject ID"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

        var entity = StudygroupEntity.builder()
                .subjectEntity(subjectEntity)
                .groupName(request.getGroupName())
                .maxMembers(request.getMaxMembers())
                .currentMembers(1)
                .status(StudygroupEntity.GroupStatus.RECRUITING)
                .build();

        entity = studygroupRepository.save(entity);

        // 생성자를 리더로 설정
        GroupMember leader = GroupMember.builder()
                .group(entity)
                .user(user)
                .role(MemberRole.LEADER)
                .build();

        groupMemberRepository.save(leader);

        return entity;
    }



    // 모든 스터디 그룹을 게시판으로 정렬
    public Content<List<StudygroupEntity>> articles(Pageable pageable) {
        var list = studygroupRepository.findAll(pageable);

        var pagination = Pagination.builder()
                .page(list.getNumber())
                .size(list.getSize())
                .currentElements(list.getNumberOfElements())
                .totalElements(list.getTotalElements())
                .totalPage(list.getTotalPages())
                .build()
                ;

        var response = Content.<List<StudygroupEntity>>builder()
                .body(list.toList())
                .pagination(pagination)
                .build();

        return response;

    }
    // 검색 기능 추가
    public List<StudygroupDto> searchStudyGroups(String keyword, String searchType) {
        List<StudygroupEntity> results;
        if ("subject".equals(searchType)) {
            results = studygroupRepository.findBySubjectEntity_SubjectNameContainingIgnoreCase(keyword);
        } else {
            results = studygroupRepository.findByGroupNameContainingIgnoreCase(keyword);
        }

        // null 값 처리를 포함한 DTO 변환
        return results.stream()
                .map(entity -> StudygroupDto.builder()
                        .id(entity.getId())
                        .groupName(entity.getGroupName())
                        .subjectName(entity.getSubjectEntity() != null ?
                                entity.getSubjectEntity().getSubjectName() : "미지정")
                        .currentMembers(entity.getCurrentMembers() != null ?
                                entity.getCurrentMembers() : 1)
                        .maxMembers(entity.getMaxMembers() != null ?
                                entity.getMaxMembers() : 5)
                        .status(entity.getStatus() != null ?
                                entity.getStatus().name() : "RECRUITING")
                        .build())
                .collect(Collectors.toList());
    }

    // 상태별 스터디 그룹 조회
    public Content<List<StudygroupDto>> getStudyGroupsByStatus(Pageable pageable) {
        var page = studygroupRepository.findAll(pageable);

        var dtoList = page.getContent().stream()
                .map(studygroupConverter::toDto)
                .collect(Collectors.toList());

        return Content.<List<StudygroupDto>>builder()
                .body(dtoList)
                .pagination(Pagination.builder()
                        .page(page.getNumber())
                        .size(page.getSize())
                        .totalElements(page.getTotalElements())
                        .totalPage(page.getTotalPages())
                        .currentElements(page.getNumberOfElements())
                        .build())
                .build();
    }



    // 특정 과목의 스터디 그룹 검색
    public List<StudygroupEntity> searchBySubject(Long subjectId) {
        return studygroupRepository.findBySubjectEntity_Id(subjectId);
    }


    @Transactional
    public StudygroupDto joinGroup(Long groupId, Long userId) {
        StudygroupEntity group = studygroupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found: " + groupId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

        // 이미 가입했는지 확인
        if (groupMemberRepository.existsByGroupAndUser(group, user)) {
            throw new AlreadyJoinedException("User already joined this group");
        }

        if (group.getStatus() != StudygroupEntity.GroupStatus.RECRUITING) {
            throw new GroupNotRecruitingException("This group is not recruiting");
        }

        if (group.getCurrentMembers() >= group.getMaxMembers()) {
            group.setStatus(StudygroupEntity.GroupStatus.FULL);
            throw new GroupFullException("This group is already full");
        }

        // 멤버 추가
        GroupMember member = GroupMember.builder()
                .group(group)
                .user(user)
                .role(MemberRole.MEMBER)
                .build();

        groupMemberRepository.save(member);

        group.setCurrentMembers(group.getCurrentMembers() + 1);
        if (group.getCurrentMembers().equals(group.getMaxMembers())) {
            group.setStatus(StudygroupEntity.GroupStatus.FULL);
        }

        return studygroupConverter.toDto(studygroupRepository.save(group));
    }


    @Transactional
    public StudygroupDto updateGroupInfo(Long groupId, StudygroupRequest request, Long userId) {  // userId 파라미터 추가
        StudygroupEntity group = studygroupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found: " + groupId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

        // 현재 사용자가 그룹의 리더인지 확인
        GroupMember member = groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new AccessDeniedException("You are not a member of this group"));

        if (member.getRole() != MemberRole.LEADER) {
            throw new AccessDeniedException("Only the group leader can update the group");
        }

        group.setGroupName(request.getGroupName());
        group.setMaxMembers(request.getMaxMembers());

        return studygroupConverter.toDto(studygroupRepository.save(group));
    }


    @Transactional
    public void deleteGroup(Long groupId, Long userId) {  // userId 파라미터 추가
        StudygroupEntity group = studygroupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found: " + groupId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

        // 현재 사용자가 그룹의 리더인지 확인
        GroupMember member = groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new AccessDeniedException("You are not a member of this group"));

        if (member.getRole() != MemberRole.LEADER) {
            throw new AccessDeniedException("Only the group leader can delete the group");
        }

        studygroupRepository.delete(group);
    }


    public Map<String, List<StudygroupDto>> getUserStudyGroups(Long userId) {
        List<GroupMember> userGroups = groupMemberRepository.findByUser_Id(userId);

        Map<String, List<StudygroupDto>> result = new HashMap<>();

        // 내가 리더인 그룹들
        result.put("myGroups", userGroups.stream()
                .filter(gm -> gm.getRole() == MemberRole.LEADER)
                .map(GroupMember::getGroup)
                .map(studygroupConverter::toDto)
                .collect(Collectors.toList()));

        // 내가 멤버로 참여중인 그룹들
        result.put("joinedGroups", userGroups.stream()
                .filter(gm -> gm.getRole() == MemberRole.MEMBER)
                .map(GroupMember::getGroup)
                .map(studygroupConverter::toDto)
                .collect(Collectors.toList()));

        return result;
    }

    public StudygroupDto getStudyGroupDto(Long groupId) {
        StudygroupEntity group = studygroupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("스터디 그룹을 찾을 수 없습니다: " + groupId));
        return studygroupConverter.toDto(group);
    }

    public StudygroupEntity getStudyGroup(Long groupId) {
        return studygroupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("스터디 그룹을 찾을 수 없습니다: " + groupId));
    }



}
