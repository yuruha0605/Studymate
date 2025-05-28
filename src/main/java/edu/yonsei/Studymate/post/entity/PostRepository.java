package edu.yonsei.Studymate.post.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findAllByBoardEntityId(Long boardId, Pageable pageable);

    // 스터디그룹별 게시글 조회 (ID 역순)
    @Query("SELECT p FROM t_bbs2025 p WHERE p.boardEntity.studygroup.id = :studygroupId ORDER BY p.id DESC")
    Page<PostEntity> findAllByStudyRoomIdOrderByIdDesc(@Param("studygroupId") Long studygroupId, Pageable pageable);

    // 스터디그룹별 게시글 수 조회
    @Query("SELECT COUNT(p) FROM t_bbs2025 p WHERE p.boardEntity.studygroup.id = :studygroupId")
    long countByStudyRoomId(@Param("studygroupId") Long studygroupId);
}


