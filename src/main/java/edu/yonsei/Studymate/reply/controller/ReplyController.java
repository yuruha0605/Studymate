package edu.yonsei.Studymate.reply.controller;

import edu.yonsei.Studymate.reply.dto.ReplyDto;
import edu.yonsei.Studymate.reply.dto.ReplyRequest;
import edu.yonsei.Studymate.reply.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/ch10/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping(path="/create")
    public RedirectView create(
            @Valid
            @ModelAttribute ReplyRequest replyRequest
    ){
        ReplyDto replyDto = replyService.create(replyRequest);
        return new RedirectView("/ch10/post/articles?page=0&size=10");
    }

}