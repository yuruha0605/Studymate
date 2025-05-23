package edu.yonsei.Studymate.post.service;

import edu.yonsei.Studymate.board.entity.BoardRepository;
import edu.yonsei.Studymate.common.Content;
import edu.yonsei.Studymate.common.Pagination;
import edu.yonsei.Studymate.post.dto.PostDeleteRequest;
import edu.yonsei.Studymate.post.dto.PostRequest;
import edu.yonsei.Studymate.post.entity.PostEntity;
import edu.yonsei.Studymate.post.entity.PostRepository;
import lombok.RequiredArgsConstructor;
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

    public PostEntity create(
            PostRequest postRequest
    ){
        var boardEntity = boardRepository.findById(postRequest.getBoardId()).get();

        var entity = PostEntity.builder()
                .boardEntity(boardEntity)
                .subject(postRequest.getSubject()) // 이거 나중에 title로 바꿀 것
                .content(postRequest.getContent())
                .written(LocalDateTime.now())
                .build()
                ;

        return postRepository.save(entity);
    }

    public void delete(PostDeleteRequest postDeleteRequest) {
        postRepository.findById(postDeleteRequest.getPostId())
                .map( it -> {
                    postRepository.delete(it);
                    return it;
                }).orElseThrow(
                        ()-> {
                            return new RuntimeException("해당 게시글이 존재 하지 않습니다 : " +
                                        postDeleteRequest.getPostId());
                        }
                );
    }

    public Content<List<PostEntity>> articles(Pageable pageable) {
        var list = postRepository.findAll(pageable);

        var pagination = Pagination.builder()
                .page(list.getNumber())
                .size(list.getSize())
                .currentElements(list.getNumberOfElements())
                .totalElements(list.getTotalElements())
                .totalPage(list.getTotalPages())
                .build()
                ;

        var response = Content.<List<PostEntity>>builder()
                .body(list.toList())
                .pagination(pagination)
                .build();

        return response;
    }

    public PostEntity getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + postId));
    }

    @Transactional
    public void update(PostRequest postRequest) {

        var post = postRepository.findById(postRequest.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        post.setSubject(postRequest.getSubject());
        post.setContent(postRequest.getContent());
    }
}