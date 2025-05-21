package edu.yonsei.Studymate.post.controller;

import edu.yonsei.Studymate.common.Content;
import edu.yonsei.Studymate.post.dto.PostDeleteRequest;
import edu.yonsei.Studymate.post.dto.PostRequest;
import edu.yonsei.Studymate.post.entity.PostEntity;
import edu.yonsei.Studymate.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ch10/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping(path="/create")

    public String create(
            @Valid
            @ModelAttribute PostRequest postRequest
    ){
        postService.create(postRequest);

        return "redirect:/ch10/post/articles?page=0&size=10";
    }

    @GetMapping(path="/articles")
    public String list (
            @RequestParam int page,
            @RequestParam int size,
            Model model
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Content<List<PostEntity>> content = postService.articles(pageable);
        model.addAttribute("bbsArticle", content);

        return "bbs";
    }

    @PostMapping("/delete")
    public String delete(
            @Valid
            @ModelAttribute PostDeleteRequest postDeleteRequest
    ){
        postService.delete(postDeleteRequest);

        return "redirect:/ch10/post/articles?page=0&size=10";

    }

    @PostMapping(path = "/read")
    public String updateForm(PostRequest postRequest, Model model) {

        PostEntity post = postService.getPostById(postRequest.getBoardId());
        postRequest.setSubject(post.getSubject());
        postRequest.setContent(post.getContent());
        model.addAttribute("postRequest", postRequest);

        return "update";
    }

    @GetMapping("/write")
    public String write (

    ){
        return "post";
    }

    @PostMapping(path="/update")
    public String update(
            @Valid
            @ModelAttribute PostRequest postRequest
    ){
        postService.update(postRequest);

        return "redirect:/ch10/post/articles?page=0&size=10";
    }

}

