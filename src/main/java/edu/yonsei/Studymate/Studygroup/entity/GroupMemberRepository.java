package edu.yonsei.Studymate.Studygroup.entity;

import edu.yonsei.Studymate.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    boolean existsByGroupAndUser(StudygroupEntity group, User user);
    Optional<GroupMember> findByGroupAndUser(StudygroupEntity group, User user);
    List<GroupMember> findByGroup(StudygroupEntity group);
    Optional<GroupMember> findByGroupAndRole(StudygroupEntity group, MemberRole role);
}


