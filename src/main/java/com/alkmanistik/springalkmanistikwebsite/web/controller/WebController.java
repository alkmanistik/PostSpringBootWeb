package com.alkmanistik.springalkmanistikwebsite.web.controller;

import com.alkmanistik.springalkmanistikwebsite.model.Comment;
import com.alkmanistik.springalkmanistikwebsite.service.CommentService;
import com.alkmanistik.springalkmanistikwebsite.service.PostService;
import com.alkmanistik.springalkmanistikwebsite.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class WebController {

    private final PostService postService;

    private final CommentService commentService;

    @GetMapping
    public String indexPage(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "index";
    }

    @GetMapping("/posts/new")
    public String newPost(Model model) {
        model.addAttribute("post", new PostDto());
        return "new-post";
    }

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        var post = postService.getPostById(id);
        var comments = commentService.findAllByPostId(id);
        model.addAttribute("post", post);
        model.addAttribute("comment", new Comment());
        model.addAttribute("comments", comments);
        return "post";
    }

    @PostMapping("/posts")
    public String newPost(@ModelAttribute("post") PostDto postDto) {
        var post = postService.createPost(postDto);
        return "redirect:/posts/" + post.getId();
    }

    @PostMapping("/comments/{postId}")
    public String addComment(
            @ModelAttribute Comment comment,
            @PathVariable Long postId
    ) {
        commentService.createComment(comment, postId);
        return "redirect:/posts/" + postId;
    }

}
