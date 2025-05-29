//package edu.yonsei.Studymate.Studygroup.entity;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface StudygroupRepository extends JpaRepository<StudygroupEntity, Long>  {
//
//    Optional<StudygroupEntity> findByGroupName(String name);
//    // 검색 기능을 위한 메서드 추가
//    List<StudygroupEntity> findByGroupNameContainingIgnoreCase(String keyword);
//
//    // 과목 ID로 검색
//    List<StudygroupEntity> findBySubjectEntity_Id(Long subjectId);
//
//    // 과목명으로 검색
//    List<StudygroupEntity> findBySubjectEntity_SubjectNameContainingIgnoreCase(String subjectName);
//
//    // 상태별 검색
//    List<StudygroupEntity> findByStatus(StudygroupEntity.GroupStatus status);
//
//    // 스터디 그룹 랜덤 5개 검색
//    @Query(value = "SELECT * FROM t_studygroup ORDER BY RAND() LIMIT 5", nativeQuery = true)
//    List<StudygroupEntity> findRandom5();
//}