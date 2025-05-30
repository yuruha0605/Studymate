package edu.yonsei.Studymate.Studygroup.service;


import edu.yonsei.Studymate.Myclass.entity.MateEntity;
import edu.yonsei.Studymate.Myclass.repository.MateRepository;
import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import edu.yonsei.Studymate.Studygroup.dto.StudygroupRequest;
import edu.yonsei.Studymate.Studygroup.entity.*;
import edu.yonsei.Studymate.Studygroup.exception.*;
import edu.yonsei.Studymate.board.entity.BoardEntity;
import edu.yonsei.Studymate.board.entity.BoardRepository;
import edu.yonsei.Studymate.common.Content;
import edu.yonsei.Studymate.common.Pagination;
import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.repository.UserRepository;
import edu.yonsei.Studymate.post.entity.PostEntity;
import edu.yonsei.Studymate.post.entity.PostRepository;
import edu.yonsei.Studymate.schedule.entity.ScheduleRepository;
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
import java.util.Optional;
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
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final ScheduleRepository scheduleRepository;
    private final MateRepository mateRepository;


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
                .orElseThrow(() -> new GroupNotFoundException("스터디 그룹을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));


        // 이미 참여 중인지 확인
        if (groupMemberRepository.existsByGroupAndUser(group, user)) {
            throw new AlreadyJoinedException("이미 참여 중인 스터디 그룹입니다.");
        }

        // 인원 수 확인
        if (group.getCurrentMembers() >= group.getMaxMembers()) {
            throw new GroupFullException("스터디 그룹의 인원이 가득 찼습니다.");
        }

        // 모집 상태 확인
        if (group.getStatus() != StudygroupEntity.GroupStatus.RECRUITING) {
            throw new GroupNotRecruitingException("현재 모집 중이 아닌 스터디 그룹입니다.");
        }

        // 멤버 추가
        GroupMember newMember = GroupMember.builder()
                .group(group)
                .user(user)
                .role(MemberRole.MEMBER)
                .build();

        // MateEntity 생성 및 저장
        mateRepository.save(MateEntity.builder()
                .studygroup(group)
                .user(user)
                .isOnline(true)
                .lastActiveTime(LocalDateTime.now())
                .build());


        groupMemberRepository.save(newMember);

        // 현재 인원 수 증가
        group.setCurrentMembers(group.getCurrentMembers() + 1);

        // 인원이 가득 찼다면 상태 변경
        if (group.getCurrentMembers().equals(group.getMaxMembers())) {
            group.setStatus(StudygroupEntity.GroupStatus.FULL);
        }

        studygroupRepository.save(group);

        return studygroupConverter.toDto(group, user);
    }


    @Transactional
    public StudygroupDto updateGroupInfo(Long groupId, StudygroupRequest request, Long userId) {
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

        // 새로운 최대 인원이 현재 인원보다 작은 경우 예외 처리
        if (request.getMaxMembers() < group.getCurrentMembers()) {
            throw new IllegalArgumentException("새로운 최대 인원이 현재 인원보다 작을 수 없습니다.");
        }

        group.setGroupName(request.getGroupName());
        group.setMaxMembers(request.getMaxMembers());

        // 상태 업데이트
        if (group.getCurrentMembers().equals(group.getMaxMembers())) {
            group.setStatus(StudygroupEntity.GroupStatus.FULL);
        } else {
            group.setStatus(StudygroupEntity.GroupStatus.RECRUITING);
        }

        return studygroupConverter.toDto(studygroupRepository.save(group));
    }



    @Transactional
    public void deleteGroup(Long groupId, Long userId) {
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

        // 1. 연관된 스케줄 삭제
        scheduleRepository.deleteAllByStudygroupId(groupId);

        // 2. 연관된 게시판의 게시글 삭제
        BoardEntity board = boardRepository.findByStudygroupId(groupId).orElse(null);
        if (board != null) {
            // 게시글 삭제
            postRepository.deleteAllByBoardId(board.getId());
            // 게시판 삭제
            boardRepository.delete(board);
        }

        // 3. 그룹 멤버 삭제
        groupMemberRepository.deleteAll(group.getMembers());

        // 4. 마지막으로 스터디 그룹 삭제
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


    public List<StudygroupDto> getRandomStudyGroups() {
        return studygroupRepository.findRandom6().stream()  // 메서드 호출 변경
                .map(studygroupConverter::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeAllStudyMates(Long groupId, Long userId) {
        StudygroupEntity group = getStudyGroup(groupId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 권한 체크 (방장만 가능)
        Optional<GroupMember> leaderMember = groupMemberRepository.findByGroupAndRole(group, MemberRole.LEADER);
        if (leaderMember.isEmpty() || !leaderMember.get().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("스터디 삭제 권한이 없습니다.");
        }

        // 모든 멤버 관계 삭제
        List<GroupMember> members = groupMemberRepository.findByGroup(group);
        groupMemberRepository.deleteAll(members);

        // 스터디 그룹 삭제
        studygroupRepository.delete(group);
    }

    @Transactional
    public void leaveGroup(Long groupId, Long userId) {
        StudygroupEntity group = getStudyGroup(groupId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        GroupMember member = groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new IllegalStateException("해당 스터디 그룹의 멤버가 아닙니다."));

        // 방장은 탈퇴할 수 없음
        if (member.getRole() == MemberRole.LEADER) {
            throw new IllegalStateException("방장은 스터디를 탈퇴할 수 없습니다. 스터디를 삭제해주세요.");
        }

        // 멤버 관계 삭제
        groupMemberRepository.delete(member);

        // 현재 인원 감소
        group.setCurrentMembers(group.getCurrentMembers() - 1);

        // 모집 상태 업데이트
        if (group.getCurrentMembers() < group.getMaxMembers()) {
            group.setStatus(StudygroupEntity.GroupStatus.RECRUITING);
        }

        studygroupRepository.save(group);
    }

    // 온라인 상태의 스터디 메이트 조회
    public List<GroupMember> getOnlineStudyMates(Long userId, int limit) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);

        // 사용자가 속한 모든 스터디 그룹의 멤버들을 조회
        List<StudygroupEntity> userGroups = studygroupRepository.findByMemberUser(user);

        // 모든 멤버들을 수집하고 중복 제거
        Map<Long, GroupMember> uniqueMembers = new HashMap<>();

        for (StudygroupEntity group : userGroups) {
            List<GroupMember> members = groupMemberRepository.findByGroup(group);
            for (GroupMember member : members) {
                if (!member.getUser().getId().equals(userId)) {  // 자신 제외
                    uniqueMembers.putIfAbsent(member.getUser().getId(), member);
                }
            }
        }

        // 온라인 상태 우선으로 정렬하여 반환
        return uniqueMembers.values().stream()
                .sorted((a, b) -> {
                    boolean aOnline = isUserOnline(a.getUser().getId(), fiveMinutesAgo);
                    boolean bOnline = isUserOnline(b.getUser().getId(), fiveMinutesAgo);
                    if (aOnline != bOnline) {
                        return aOnline ? -1 : 1;
                    }
                    return 0;
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    private boolean isUserOnline(Long userId, LocalDateTime fiveMinutesAgo) {
        // 여기에서 사용자의 마지막 활동 시간을 확인하는 로직 구현
        List<MateEntity> userMates = mateRepository.findByUser_Id(userId);
        return userMates.stream()
                .anyMatch(mate -> mate.getLastActiveTime().isAfter(fiveMinutesAgo));
    }
}


