package edu.yonsei.Studymate.Myclass.repository;

import edu.yonsei.Studymate.Myclass.entity.MyclassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MyclassRepository extends JpaRepository<MyclassEntity, Long> {
    Optional<MyclassEntity> findByName(String name);
}
