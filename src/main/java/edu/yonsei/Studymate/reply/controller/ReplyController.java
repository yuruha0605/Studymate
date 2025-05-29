package edu.yonsei.Studymate.reply.controller;

import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.security.CustomUserDetails;
import edu.yonsei.Studymate.post.entity.PostRepository;
import edu.yonsei.Studymate.reply.dto.ReplyDto;
import edu.yonsei.Studymate.reply.dto.ReplyRequest;
import edu.yonsei.Studymate.reply.service.ReplyConverter;
import edu.yonsei.Studymate.reply.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/study-mate/replies")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    private final PostRepository postRepository;
    private final ReplyConverter replyConverter;

    @PostMapping("/create")
    public ResponseEntity<ReplyDto> create(
            @Valid @RequestBody ReplyRequest replyRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User currentUser = userDetails.getUser();
        ReplyDto replyDto = replyService.create(replyRequest, currentUser);
        return ResponseEntity.ok(replyDto);
    }


    @GetMapping("/post/{postId}")
    public ResponseEntity<List<ReplyDto>> getRepliesByPost(@PathVariable Long postId) {
        var postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        var replies = replyService.findAllByPostEntityOrderById(postEntity)
                .stream()
                .map(replyConverter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(replies);
    }
}
