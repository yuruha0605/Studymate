package edu.yonsei.Studymate.repository;

import edu.yonsei.Studymate.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
}
