package edu.yonsei.Studymate.team.repository;

import edu.yonsei.Studymate.team.entity.TeamEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
}


// 수정 한 번 해보기