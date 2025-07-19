package org.example.blogapplication.repository;

import org.example.blogapplication.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId); // To get comments for a specific post
}
