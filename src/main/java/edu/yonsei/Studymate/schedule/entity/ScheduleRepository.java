package edu.yonsei.Studymate.schedule.entity;

import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import org.springframework.data.jpa.repository.Modifying;  // 이 부분이 변경됨
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    @Query("SELECT s FROM t_schedule s WHERE s.studygroup.id = :studygroupId ORDER BY s.scheduleDateTime ASC")
    List<ScheduleEntity> findByStudygroupIdOrderByScheduleDateTimeAsc(@Param("studygroupId") Long studygroupId);

    @Modifying
    @Query("DELETE FROM t_schedule s WHERE s.studygroup.id = :studygroupId")
    void deleteAllByStudygroupId(@Param("studygroupId") Long studygroupId);

    @Query("SELECT s FROM t_schedule s " +
            "JOIN s.studygroup sg " +
            "WHERE sg IN :userGroups " +
            "AND s.scheduleDateTime >= :now " +
            "ORDER BY s.scheduleDateTime ASC")
    List<ScheduleEntity> findUpcomingSchedulesByStudygroups(
            @Param("userGroups") List<StudygroupEntity> userGroups,
            @Param("now") LocalDateTime now
    );


}




