package edu.yonsei.Studymate.post.controller;

import edu.yonsei.Studymate.board.entity.BoardEntity;
import edu.yonsei.Studymate.board.entity.BoardRepository;
import edu.yonsei.Studymate.common.ApiUrls;
import edu.yonsei.Studymate.common.Content;
import edu.yonsei.Studymate.common.Pagination;
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


    @PostMapping(ApiUrls.Post.CREATE)   // "~/create"
    public PostDto create(
            @Valid
            @RequestBody PostRequest postRequest  // @ModelAttribute -> @RequestBody
    ){
        PostEntity entity = postService.create(postRequest);
        return postConverter.toDto(entity);
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




    @DeleteMapping(ApiUrls.Post.DELETE)   // "/{postId}/delete"
    public void delete(
            @PathVariable Long postId
    ){
        postService.delete(new PostDeleteRequest(postId));
    }

    @GetMapping(ApiUrls.Post.DETAIL)
    public PostDto getPost(@PathVariable Long postId) {
        return postConverter.toDto(postService.getPost(postId));
    }


    @PutMapping(ApiUrls.Post.UPDATE)
    public PostDto update(
            @PathVariable Long postId,
            @Valid @RequestBody PostRequest postRequest
    ) {
        return postConverter.toDto(postService.update(postRequest));
    }

    // View 관련 메서드들은 별도의 Controller로 분리하는 것이 좋습니다
    @GetMapping("/write")
    public String write() {
        return "post";
    }



}

