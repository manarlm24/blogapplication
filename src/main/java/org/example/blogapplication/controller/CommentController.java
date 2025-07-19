package org.example.blogapplication.controller;

import org.example.blogapplication.models.Comment;
import org.example.blogapplication.models.Post;
import org.example.blogapplication.models.User;
import org.example.blogapplication.services.CommentService;
import org.example.blogapplication.services.PostService;
import org.example.blogapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/add/{postId}")
    public String addComment(@PathVariable Long postId, @RequestParam String content, Principal principal) {
        Post post = postService.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setAuthor(user);
        comment.setCreatedAt(LocalDateTime.now());

        commentService.saveComment(comment);

        return "redirect:/posts/" + postId; // Redirect back to the post page
    }
/*
    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId, @RequestParam Long postId) {
        commentService.deleteComment(commentId);
        return "redirect:/posts/" + postId;
    }
*/
@PostMapping("/delete/{commentId}")
public String deleteComment(@PathVariable Long commentId, @RequestParam Long postId, Principal principal) {
    Comment comment = commentService.findById(commentId);
    Post post = postService.getPostById(postId).orElse(null);

    if (comment != null && post != null) {
        String currentUsername = principal.getName();
        if (post.getAuthor().getUsername().equals(currentUsername)) {
            commentService.deleteComment(commentId);
        }
    }

    return "redirect:/posts/" + postId;
}


    // Later you can add an edit method for comments too!
}
