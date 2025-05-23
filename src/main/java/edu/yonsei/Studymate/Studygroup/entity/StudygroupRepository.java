package edu.yonsei.Studymate.Studygroup.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudygroupRepository extends JpaRepository<StudygroupEntity, Long>  {

    Optional<StudygroupEntity> findByGroupName(String name);
}
