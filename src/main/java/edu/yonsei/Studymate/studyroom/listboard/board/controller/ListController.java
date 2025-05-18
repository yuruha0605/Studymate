package edu.yonsei.Studymate.studyroom.listboard.board.controller;


import edu.yonsei.Studymate.studyroom.listboard.board.dto.ListDto;
import edu.yonsei.Studymate.studyroom.listboard.board.dto.ListRequest;
import edu.yonsei.Studymate.studyroom.listboard.board.service.ListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/yonseit8/studyroom")

public class ListController {

    private final ListService listService;


    // 새로운 게시글 create
    @PostMapping(path = "list/create")
    public ListDto func1 (
            @Valid
            @RequestBody ListRequest listRequest
    ){
        return ListService.create(listRequest);
    }

    // get해서 출력
    @GetMapping(path = "/list/article/{id}")
    public ListDto view(
            @PathVariable Long id
    ){
        var entity = ListService.article(id);
        return entity;
    }



}
