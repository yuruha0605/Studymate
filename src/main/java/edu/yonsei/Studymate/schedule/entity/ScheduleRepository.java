package edu.yonsei.Studymate.schedule.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findByStudygroupIdOrderByScheduleDateTimeAsc(Long studygroupId);
}

