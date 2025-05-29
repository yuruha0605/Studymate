package edu.yonsei.Studymate.post.controller;

import edu.yonsei.Studymate.board.entity.BoardEntity;
import edu.yonsei.Studymate.board.entity.BoardRepository;
import edu.yonsei.Studymate.common.ApiUrls;
import edu.yonsei.Studymate.common.Content;
import edu.yonsei.Studymate.common.Pagination;
import edu.yonsei.Studymate.login.security.CustomUserDetails;
import edu.yonsei.Studymate.post.dto.PostDeleteRequest;
import edu.yonsei.Studymate.post.dto.PostDto;
import edu.yonsei.Studymate.post.dto.PostRequest;
import edu.yonsei.Studymate.post.entity.PostEntity;
import edu.yonsei.Studymate.post.entity.PostRepository;
import edu.yonsei.Studymate.post.service.PostConverter;
import edu.yonsei.Studymate.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiUrls.Post.BASE)  // "/api/study-mate/posts"
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostConverter postConverter;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;


    @PostMapping(ApiUrls.Post.CREATE)
    public PostDto create(
            @Valid @RequestBody PostRequest postRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails  // 현재 사용자 정보 주입
    ) {
        return postService.create(postRequest, userDetails.getUser());
    }




    @GetMapping(ApiUrls.Post.LIST)  // "~/{groupId}/list"
    public Content<List<PostDto>> list(
            @PathVariable Long groupId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        // 해당 스터디그룹의 게시판에 속한 게시글만 조회
        BoardEntity board = boardRepository.findByStudygroupId(groupId)
                .orElseThrow(() -> new IllegalArgumentException("게시판을 찾을 수 없습니다."));

        Page<PostEntity> postPage = postRepository.findAllByBoardIdOrderByIdDesc(board.getId(), pageable);

        List<PostDto> dtoList = postPage.getContent().stream()
                .map(postConverter::toDto)
                .collect(Collectors.toList());

        return Content.<List<PostDto>>builder()
                .body(dtoList)
                .pagination(Pagination.builder()
                        .page(postPage.getNumber())
                        .size(postPage.getSize())
                        .totalElements(postPage.getTotalElements())
                        .totalPage(postPage.getTotalPages())
                        .currentElements(postPage.getNumberOfElements())
                        .build())
                .build();
    }




    @DeleteMapping("/{postId}")  // DELETE 메소드 매핑 수정
    public void delete(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        postService.delete(postId, userDetails.getUser());
    }


    @GetMapping("/{postId}")  // 단순히 /{postId}로 수정
    public PostDto getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }


    @PutMapping("/{postId}")  // PUT 메소드 매핑 추가
    public PostDto update(
            @PathVariable Long postId,
            @Valid @RequestBody PostRequest postRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return postService.update(postId, postRequest, userDetails.getUser());
    }




    // View 관련 메서드들은 별도의 Controller로 분리하는 것이 좋습니다
    @GetMapping("/write")
    public String write() {
        return "post";
    }



}

