package edu.yonsei.Studymate.board.service;

import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupRepository;
import edu.yonsei.Studymate.board.dto.BoardDto;
import edu.yonsei.Studymate.board.dto.BoardRequest;
import edu.yonsei.Studymate.board.entity.BoardEntity;
import edu.yonsei.Studymate.board.entity.BoardRepository;
import edu.yonsei.Studymate.common.Content;
import edu.yonsei.Studymate.post.entity.PostEntity;
import edu.yonsei.Studymate.post.service.PostConverter;
import edu.yonsei.Studymate.post.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardConverter boardConverter;
    private final StudygroupRepository studygroupRepository;
    private final PostService postService;
    private final PostConverter postConverter;


    @Transactional
    public BoardDto create(BoardRequest boardRequest, Long studygroupId) {
        StudygroupEntity studygroup = studygroupRepository.findById(studygroupId)
                .orElseThrow(() -> new IllegalArgumentException("Study group not found: " + studygroupId));

        if (boardRepository.findByStudygroupId(studygroupId).isPresent()) {
            throw new IllegalStateException("Board already exists for this study group");
        }

        var entity = BoardEntity.builder()
                .boardName(boardRequest.getBoardName())
                .studygroup(studygroup)
                .build();

        return boardConverter.toDto(boardRepository.save(entity));
    }



    public BoardDto getByStudygroup(Long studygroupId) {
        log.info("Getting board for studygroup: {}", studygroupId);
        try {
            BoardEntity board = boardRepository.findByStudygroupId(studygroupId)
                    .orElseThrow(() -> new IllegalArgumentException("게시판을 찾을 수 없습니다."));
            return boardConverter.toDto(board);
        } catch (Exception e) {
            log.error("Error getting board: ", e);
            throw e;
        }
    }

    public Content<BoardDto> getBoardWithPosts(Long studygroupId, int page, int size) {
        // 보드 조회 또는 생성
        BoardEntity board = boardRepository.findByStudygroupId(studygroupId)
                .orElseGet(() -> {
                    StudygroupEntity studygroup = studygroupRepository.findById(studygroupId)
                            .orElseThrow(() -> new IllegalArgumentException("Study group not found: " + studygroupId));

                    return boardRepository.save(BoardEntity.builder()
                            .boardName(studygroup.getGroupName() + " 게시판")
                            .studygroup(studygroup)
                            .build());
                });

        // 게시글 페이지네이션 조회
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Content<List<PostEntity>> postContent = postService.articles(pageable);

        // BoardDto 생성 및 반환
        BoardDto boardDto = boardConverter.toDto(board);
        boardDto.setPostList(postContent.getBody().stream()
                .filter(post -> post.getBoard().getId().equals(board.getId()))
                .map(postConverter::toDto)
                .collect(Collectors.toList()));

        return Content.<BoardDto>builder()
                .body(boardDto)
                .pagination(postContent.getPagination())
                .build();

    }

}

