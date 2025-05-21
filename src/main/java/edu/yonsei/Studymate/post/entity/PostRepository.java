package edu.yonsei.Studymate.post.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Optional<PostEntity> findFirstByIdOrderByIdDesc(Long id);
}
