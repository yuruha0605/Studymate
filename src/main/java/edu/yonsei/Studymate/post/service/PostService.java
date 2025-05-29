package edu.yonsei.Studymate.post.service;

import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupRepository;
import edu.yonsei.Studymate.board.entity.BoardEntity;
import edu.yonsei.Studymate.board.entity.BoardRepository;
import edu.yonsei.Studymate.common.Content;
import edu.yonsei.Studymate.common.Pagination;
import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.post.dto.PostDeleteRequest;
import edu.yonsei.Studymate.post.dto.PostDto;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final PostConverter postConverter;

    @Transactional
    public PostDto create(PostRequest request, User currentUser) {
        BoardEntity board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        PostEntity post = PostEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(currentUser)  // 현재 로그인한 사용자를 작성자로 설정
                .board(board)
                .build();

        return postConverter.toDto(postRepository.save(post));
    }




    @Transactional(readOnly = true)
    public Content<List<PostDto>> getStudyRoomPosts(Long studyRoomId, Pageable pageable) {
        BoardEntity board = boardRepository.findByStudygroupId(studyRoomId)
                .orElseThrow(() -> new IllegalArgumentException("게시판을 찾을 수 없습니다."));

        Page<PostEntity> page = postRepository.findAllByBoardIdOrderByIdDesc(board.getId(), pageable);
        List<PostDto> dtos = page.getContent().stream()
                .map(postConverter::toDto)
                .collect(Collectors.toList());

        return Content.<List<PostDto>>builder()
                .body(dtos)
                .pagination(Pagination.builder()
                        .page(page.getNumber())
                        .size(page.getSize())
                        .totalElements(page.getTotalElements())
                        .totalPage(page.getTotalPages())
                        .currentElements(page.getNumberOfElements())
                        .build())
                .build();
    }





    @Transactional
    public void delete(Long postId, User currentUser) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 작성자 본인만 삭제 가능
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("게시글 작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }


    // 다른 메서드들도 Entity 대신 Dto를 반환하도록 수정
    @Transactional(readOnly = true)
    public PostDto getPost(Long postId) {
        return postRepository.findById(postId)
                .map(postConverter::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }





    @Transactional
    public PostDto update(Long postId, PostRequest request, User currentUser) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 작성자 본인만 수정 가능
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("게시글 작성자만 수정할 수 있습니다.");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        return postConverter.toDto(post);
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
