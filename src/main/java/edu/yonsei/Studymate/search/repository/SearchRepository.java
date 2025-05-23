package edu.yonsei.Studymate.search.repository;

import edu.yonsei.Studymate.search.entity.SearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchRepository extends JpaRepository<SearchEntity, Long> {

    // 과목명으로 검색 (부분일치)
    List<SearchEntity> findByNameContainingIgnoreCase(String name);

    // 교수명으로 검색 (부분일치)
    List<SearchEntity> findByProfessorContainingIgnoreCase(String professor);

    Optional<SearchEntity> findByNameAndProfessor(String name, String professor);
}

