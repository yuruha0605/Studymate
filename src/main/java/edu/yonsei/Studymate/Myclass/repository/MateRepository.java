package edu.yonsei.Studymate.Myclass.repository;

import edu.yonsei.Studymate.Myclass.entity.MateEntity;
import edu.yonsei.Studymate.Myclass.entity.MyclassEntity;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MateRepository extends JpaRepository<MateEntity, Long> {
    List<MateEntity> findByStudygroup(StudygroupEntity studygroup);
    List<MateEntity> findByUser_Id(Long userId);  // student_id 대신 user_id 사용

    @Query("SELECT DISTINCT m FROM MateEntity m " +
            "LEFT JOIN FETCH m.user u " +
            "LEFT JOIN FETCH m.studygroup s " +
            "WHERE s IN (SELECT m2.studygroup FROM MateEntity m2 WHERE m2.user.id = :userId) " +
            "AND m.user.id != :userId " +
            "ORDER BY m.lastActiveTime DESC")
    List<MateEntity> findTopMatesByUserId(@Param("userId") Long userId, Pageable pageable);
}

