package edu.yonsei.Studymate.main.repository;

import edu.yonsei.Studymate.main.entity.ScheduleEntity;
import edu.yonsei.Studymate.main.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findByUser(User user);
}
