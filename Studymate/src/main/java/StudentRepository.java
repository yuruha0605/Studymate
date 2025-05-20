package edu.yonsei.Studymate.Myclass.repository;

import edu.yonsei.Studymate.Myclass.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByName(String name);
}
