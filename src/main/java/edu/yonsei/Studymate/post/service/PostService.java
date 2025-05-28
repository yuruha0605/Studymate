package edu.yonsei.Studymate.post.service;

import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupRepository;
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
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final StudygroupRepository studygroupRepository;
    private final PostConverter postConverter;

    @Transactional
    public PostEntity create(PostRequest request) {
        // 게시판 조회
        BoardEntity board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        // board에서 studygroup 정보를 가져옴
        StudygroupEntity studygroup = board.getStudygroup();
        if (studygroup == null) {
            throw new IllegalStateException("Board must be associated with a study group");
        }

        // 엔티티 생성 및 연관관계 설정
        PostEntity post = postConverter.toEntity(request);
        post.setBoard(board);
        post.setStudygroup(studygroup);  // studygroup 설정

        log.info("Creating post: {}", post);
        return postRepository.save(post);
    }


    @Transactional(readOnly = true)
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


    public Content<List<PostEntity>> getStudyRoomPosts(Long studyRoomId, Pageable pageable) {
        // 먼저 스터디룸의 게시판을 찾고
        BoardEntity board = boardRepository.findByStudygroupId(studyRoomId)
                .orElseThrow(() -> new IllegalArgumentException("게시판을 찾을 수 없습니다."));

        // 해당 게시판의 게시글들을 조회
        Page<PostEntity> page = postRepository.findAllByBoardIdOrderByIdDesc(board.getId(), pageable);

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

    public Content<List<PostEntity>> getPostsByStudygroup(Long studygroupId, Pageable pageable) {
        // 먼저 스터디그룹의 게시판을 찾습니다
        BoardEntity board = boardRepository.findByStudygroupId(studygroupId)
                .orElseThrow(() -> new IllegalArgumentException("게시판을 찾을 수 없습니다."));

        // 해당 게시판의 게시글들을 가져옵니다
        Page<PostEntity> page = postRepository.findAllByBoardIdOrderByIdDesc(board.getId(), pageable);

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



}
