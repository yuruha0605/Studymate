package edu.yonsei.Studymate.Studygroup.entity;

import edu.yonsei.Studymate.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    boolean existsByGroupAndUser(StudygroupEntity group, User user);
    Optional<GroupMember> findByGroupAndUser(StudygroupEntity group, User user);
    List<GroupMember> findByGroup(StudygroupEntity group);
    Optional<GroupMember> findByGroupAndRole(StudygroupEntity group, MemberRole role);// 새로 추가할 메서드
    List<GroupMember> findByUser_Id(Long userId);  // 특정 사용자의 모든 그룹 멤버십 조회
    List<GroupMember> findByUserAndRole(User user, MemberRole role);  // 특정 사용자의 특정 역할의 그룹 멤버십 조회


}


