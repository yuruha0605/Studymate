package edu.yonsei.Studymate.search.repository;

import edu.yonsei.Studymate.search.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {

    // 과목명으로 검색 (부분일치)
    List<SubjectEntity> findByNameContainingIgnoreCase(String name);

    // 교수명으로 검색 (부분일치)
    List<SubjectEntity> findByProfessorContainingIgnoreCase(String professor);

    Optional<SubjectEntity> findByNameAndProfessor(String name, String professor);
}

