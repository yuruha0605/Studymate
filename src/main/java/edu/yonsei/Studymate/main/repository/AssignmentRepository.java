package edu.yonsei.Studymate.main.repository;

import edu.yonsei.Studymate.main.entity.AssignmentEntity;
import edu.yonsei.Studymate.main.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Long> {
    int countByUserAndIsActiveTrue(User user);
}
