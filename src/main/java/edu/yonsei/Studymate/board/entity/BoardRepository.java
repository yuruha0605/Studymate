package edu.yonsei.Studymate.board.entity;

import org.springframework.data.jpa.repository.JpaRepository;
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
}
