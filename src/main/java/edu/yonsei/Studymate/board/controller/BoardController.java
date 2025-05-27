package edu.yonsei.Studymate.board.controller;

import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupRepository;
import edu.yonsei.Studymate.Studygroup.service.StudygroupService;
import edu.yonsei.Studymate.board.dto.BoardDto;
import edu.yonsei.Studymate.board.dto.BoardRequest;
import edu.yonsei.Studymate.board.service.BoardService;
import edu.yonsei.Studymate.common.ApiUrls;
import edu.yonsei.Studymate.common.Content;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import edu.yonsei.Studymate.Studygroup.service.StudygroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrls.Board.BASE)
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/{studygroupId}/create")
    public BoardDto createBoard(
            @PathVariable Long studygroupId,
            @Valid @RequestBody BoardRequest boardRequest
    ) {
        return boardService.create(boardRequest, studygroupId);
    }

    @GetMapping("/{studygroupId}")
    public Content<BoardDto> getBoard(
            @PathVariable Long studygroupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return boardService.getBoardWithPosts(studygroupId, page, size);
    }
}




