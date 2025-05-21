package edu.yonsei.Studymate.Myclass.repository;

import edu.yonsei.Studymate.Myclass.entity.MateEntity;
import edu.yonsei.Studymate.Myclass.entity.MyclassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MateRepository extends JpaRepository<MateEntity, Long> {
    List<MateEntity> findByMyclass(MyclassEntity myclass);
}
