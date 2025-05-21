package edu.yonsei.Studymate.board.controller;

import edu.yonsei.Studymate.board.dto.BoardDto;
import edu.yonsei.Studymate.board.dto.BoardRequest;
import edu.yonsei.Studymate.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/yonseit8/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping(path="/create")
    public BoardDto func1(

            @Valid
            @RequestBody BoardRequest boardRequest
    ){
        return boardService.create(boardRequest);
    }


    @GetMapping(path="/article/{id}")
    public BoardDto view(
            @PathVariable Long id
    ){
        var entity = boardService.article(id);
        return entity;
    }
}
