package edu.yonsei.Studymate.post.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    // boardEntityId를 board.id로 수정
    @Query("SELECT p FROM t_bbs2025 p WHERE p.board.id = :boardId ORDER BY p.id DESC")
    Page<PostEntity> findAllByBoardIdOrderByIdDesc(@Param("boardId") Long boardId, Pageable pageable);

    Page<PostEntity> findAllByBoardId(Long boardId, Pageable pageable);

}



