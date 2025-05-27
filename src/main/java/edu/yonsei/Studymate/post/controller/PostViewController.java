package edu.yonsei.Studymate.post.controller;

import edu.yonsei.Studymate.post.dto.PostRequest;
import edu.yonsei.Studymate.post.entity.PostEntity;
import edu.yonsei.Studymate.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/study-mate/posts")
@RequiredArgsConstructor
public class PostViewController {
    private final PostService postService;

    @GetMapping("/write")
    public String write() {
        return "post";
    }

    @GetMapping("/{postId}")
    public String updateForm(@PathVariable Long postId, Model model) {
        PostEntity post = postService.getPost(postId);
        model.addAttribute("postRequest", new PostRequest(post.getId(), post.getTitle(), post.getContent()));
        return "update";
    }
}
