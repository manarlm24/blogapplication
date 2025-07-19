package org.example.blogapplication.services;

import jakarta.transaction.Transactional;
import org.example.blogapplication.models.Post;
import org.example.blogapplication.models.User;
import org.example.blogapplication.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Create a new post
    public Post createPost(Post post, User author) {
        post.setAuthor(author);  // No need for Optional, just pass the author directly
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    // Get all posts
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    // Get posts by author
    public List<Post> getPostsByAuthor(User author) {
        return postRepository.findByAuthorOrderByCreatedAtDesc(author);
    }

    // Get single post by ID
    public Optional<Post> getPostById(Long id) {
        return postRepository.findByIdWithAuthor(id);
    }

    // Update existing post
    public Post updatePost(Long postId, Post updatedPost) {
        return postRepository.findById(postId)
                .map(existingPost -> {
                    if (updatedPost.getTitle() != null) {
                        existingPost.setTitle(updatedPost.getTitle());
                    }
                    if (updatedPost.getContent() != null) {
                        existingPost.setContent(updatedPost.getContent());
                    }
                    existingPost.setUpdatedAt(LocalDateTime.now());
                    return postRepository.save(existingPost);
                })
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
    }

    // Delete post
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    // Save post (generic method)
    public Post savePost(Post post) {
        return postRepository.save(post); // Must return the saved entity
    }

    // Check if post exists
    public boolean existsById(Long postId) {
        return postRepository.existsById(postId);
    }
}
