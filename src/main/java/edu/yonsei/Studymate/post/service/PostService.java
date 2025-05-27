package edu.yonsei.Studymate.post.service;

import edu.yonsei.Studymate.board.entity.BoardEntity;
import edu.yonsei.Studymate.board.entity.BoardRepository;
import edu.yonsei.Studymate.common.Content;
import edu.yonsei.Studymate.common.Pagination;
import edu.yonsei.Studymate.post.dto.PostDeleteRequest;
import edu.yonsei.Studymate.post.dto.PostRequest;
import edu.yonsei.Studymate.post.entity.PostEntity;
import edu.yonsei.Studymate.post.entity.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;

    public PostEntity create(PostRequest request) {
        BoardEntity board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시판을 찾을 수 없습니다."));

        return postRepository.save(PostEntity.builder()
                .boardEntity(board)
                .title(request.getTitle())
                .content(request.getContent())
                .written(LocalDateTime.now())
                .build());
    }

    public Content<List<PostEntity>> articles(Pageable pageable) {
        Page<PostEntity> page = postRepository.findAll(pageable);
        return Content.<List<PostEntity>>builder()
                .body(page.getContent())
                .pagination(Pagination.builder()
                        .page(page.getNumber())
                        .size(page.getSize())
                        .totalElements(page.getTotalElements())
                        .totalPage(page.getTotalPages())
                        .currentElements(page.getNumberOfElements())
                        .build())
                .build();
    }


    public void delete(PostDeleteRequest request) {
        postRepository.deleteById(request.getPostId());
    }

    public PostEntity getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
    }


    public PostEntity update(PostRequest request) {
        PostEntity post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        return postRepository.save(post);
    }

}
