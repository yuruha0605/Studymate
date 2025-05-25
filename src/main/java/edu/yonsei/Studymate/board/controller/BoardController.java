package edu.yonsei.Studymate.board.controller;

import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupRepository;
import edu.yonsei.Studymate.Studygroup.service.StudygroupService;
import edu.yonsei.Studymate.board.dto.BoardDto;
import edu.yonsei.Studymate.board.dto.BoardRequest;
import edu.yonsei.Studymate.board.service.BoardService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import edu.yonsei.Studymate.Studygroup.service.StudygroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller  // @RestController 대신 @Controller 사용
@RequestMapping("/study")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final StudygroupRepository studygroupRepository;


    @GetMapping("/room/{studygroupId}")
    public String getStudyRoom(@PathVariable Long studygroupId, Model model) {
        log.info("Accessing study room with studygroupId: {}", studygroupId);

        try {
            // 스터디 그룹 조회
            StudygroupEntity studygroup = studygroupRepository.findById(studygroupId)
                    .orElseThrow(() -> new IllegalArgumentException("스터디 그룹을 찾을 수 없습니다."));
            log.info("Found studygroup: {}", studygroup);

            // 보드 조회
            BoardDto board = null;
            try {
                board = boardService.getByStudygroup(studygroupId);
                log.info("Found board: {}", board);
            } catch (Exception e) {
                log.error("Error finding board: ", e);
                // 보드가 없으면 새로 생성
                BoardRequest boardRequest = BoardRequest.builder()
                        .boardName(studygroup.getGroupName() + " 게시판")
                        .build();
                board = boardService.create(boardRequest, studygroupId);
                log.info("Created new board: {}", board);
            }

            model.addAttribute("studygroup", studygroup);
            model.addAttribute("board", board);
            model.addAttribute("studygroupId", studygroupId);

            return "studyroom";
        } catch (Exception e) {
            log.error("Error in getStudyRoom: ", e);
            throw e;
        }
    }





}

