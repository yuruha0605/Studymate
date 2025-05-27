package edu.yonsei.Studymate.post.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findAllByBoardEntityId(Long boardId, Pageable pageable);
}

