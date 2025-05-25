package edu.yonsei.Studymate.board.entity;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Optional<BoardEntity> findByStudygroupId(Long studygroupId);  // studygroupEntityId -> studygroupId
}


