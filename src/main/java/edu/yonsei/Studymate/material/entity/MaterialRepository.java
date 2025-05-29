package edu.yonsei.Studymate.material.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<MaterialEntity, Long> {
    List<MaterialEntity> findByStudygroupId(Long studygroupId);
}
