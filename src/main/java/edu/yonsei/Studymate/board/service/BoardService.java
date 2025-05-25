package edu.yonsei.Studymate.board.service;

import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupRepository;
import edu.yonsei.Studymate.board.dto.BoardDto;
import edu.yonsei.Studymate.board.dto.BoardRequest;
import edu.yonsei.Studymate.board.entity.BoardEntity;
import edu.yonsei.Studymate.board.entity.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardConverter boardConverter;
    private final StudygroupRepository studygroupRepository; // 추가


    @Transactional
    public BoardDto create(BoardRequest boardRequest, Long studygroupId) {
        log.info("Creating board for studygroup {}: {}", studygroupId, boardRequest);
        try {
            // 스터디 그룹 찾기
            StudygroupEntity studygroup = studygroupRepository.findById(studygroupId)
                    .orElseThrow(() -> new IllegalArgumentException("Study group not found: " + studygroupId));

            // 보드 생성
            var entity = BoardEntity.builder()
                    .boardName(boardRequest.getBoardName())
                    .studygroup(studygroup)
                    .build();

            var savedEntity = boardRepository.save(entity);
            log.info("Created board entity: {}", savedEntity);
            return boardConverter.toDto(savedEntity);
        } catch (Exception e) {
            log.error("Error creating board: ", e);
            throw e;
        }
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




}

