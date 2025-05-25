package edu.yonsei.Studymate.board.controller;

import edu.yonsei.Studymate.board.dto.BoardDto;
import edu.yonsei.Studymate.board.dto.BoardRequest;
import edu.yonsei.Studymate.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class StudyRoomRestController {
    private final BoardService boardService;

    @PostMapping("/studygroups/{studygroupId}")
    public BoardDto createBoard(
            @PathVariable Long studygroupId,
            @Valid
            @RequestBody BoardRequest boardRequest
    ) {
        return boardService.create(boardRequest, studygroupId);
    }

    @GetMapping("/studygroups/{studygroupId}")
    public BoardDto getBoardByStudygroup(
            @PathVariable Long studygroupId
    ) {
        return boardService.getByStudygroup(studygroupId);
    }
}

