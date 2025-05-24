package edu.yonsei.Studymate.subject.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
    boolean existsBySubjectName(String subjectName);
    List<SubjectEntity> findBySubjectNameContainingIgnoreCase(String subjectName);
    List<SubjectEntity> findByProfessorNameContainingIgnoreCase(String professorName);
}


