package org.example.blogapplication.controller;

import org.example.blogapplication.models.Post;
import org.example.blogapplication.models.User;
import org.example.blogapplication.repository.PostRepository;
import org.example.blogapplication.services.PostService;
import org.example.blogapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;
   // private final PostRepository postRepository;
   @Autowired
   private PostRepository postRepository;  // Inject the PostRepository
    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public String listPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());

        return "posts/list";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "posts/create";
    }
    @PostMapping
    public String createPost(@ModelAttribute Post post, Principal principal) {
        // Get the currently authenticated user
        Optional<User> authorOpt = userService.findByUsername(principal.getName());

        if (authorOpt.isPresent()) {
            // Set the author as the user object directly, not wrapped in Optional
            post.setAuthor(authorOpt.get());
            post.setCreatedAt(LocalDateTime.now());
            postService.savePost(post);
            return "redirect:/dashboard";
        } else {
            // Handle case where user doesn't exist
            return "redirect:/error?message=User not found";
        }
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (postService.existsById(id)) {
            postService.deletePost(id);
            redirectAttributes.addFlashAttribute("message", "Post deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Post not found!");
        }
        return "redirect:/posts";
    }

    // Show the Edit Form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));

        model.addAttribute("post", post);
        return "posts/edit";  // this will open the 'edit.html' page
    }

    @PreAuthorize("hasRole('USER')")
    // Handle the form submission to update the Post
    @PostMapping("/{id}/edit")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post postDetails) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));

        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        // You can update other fields if needed
        post.setUpdatedAt(LocalDateTime.now());

        postRepository.save(post);
        return "redirect:/posts";  // after updating, go back to the list
    }
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        model.addAttribute("post", post);
        model.addAttribute("comments", post.getComments()); // send comments
        return "posts/view"; // create a view.html
    }


}