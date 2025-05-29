package edu.yonsei.Studymate.schedule.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    // JPQL 쿼리 수정
    @Query("SELECT s FROM t_schedule s WHERE s.studygroup.id = :studygroupId ORDER BY s.scheduleDateTime ASC")
    List<ScheduleEntity> findByStudygroupIdOrderByScheduleDateTimeAsc(@Param("studygroupId") Long studygroupId);
}


