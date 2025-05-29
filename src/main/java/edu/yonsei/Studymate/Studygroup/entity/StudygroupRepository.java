package edu.yonsei.Studymate.Studygroup.entity;

import edu.yonsei.Studymate.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudygroupRepository extends JpaRepository<StudygroupEntity, Long>  {

    Optional<StudygroupEntity> findByGroupName(String name);
    // 검색 기능을 위한 메서드 추가
    List<StudygroupEntity> findByGroupNameContainingIgnoreCase(String keyword);

    // 과목 ID로 검색
    List<StudygroupEntity> findBySubjectEntity_Id(Long subjectId);

    // 과목명으로 검색
    List<StudygroupEntity> findBySubjectEntity_SubjectNameContainingIgnoreCase(String subjectName);

    // 상태별 검색
    List<StudygroupEntity> findByStatus(StudygroupEntity.GroupStatus status);

    @Query("SELECT DISTINCT sg FROM t_studygroup sg JOIN sg.members m WHERE m.user = :user")
    List<StudygroupEntity> findByMemberUser(@Param("user") User user);

    @Query(value = "SELECT * FROM t_studygroup ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<StudygroupEntity> findRandom5();




}

