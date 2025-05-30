package edu.yonsei.Studymate.post.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    // boardEntityId를 board.id로 수정
    @Query("SELECT p FROM t_bbs2025 p WHERE p.board.id = :boardId ORDER BY p.id DESC")
    Page<PostEntity> findAllByBoardIdOrderByIdDesc(@Param("boardId") Long boardId, Pageable pageable);

    Page<PostEntity> findAllByBoardId(Long boardId, Pageable pageable);

    // 게시판에 속한 모든 게시글 삭제
    @Modifying
    @Query("DELETE FROM t_bbs2025 p WHERE p.board.id = :boardId")
    void deleteAllByBoardId(@Param("boardId") Long boardId);
}




