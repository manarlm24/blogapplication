package org.example.blogapplication.repository;

import org.example.blogapplication.models.Post;
import org.example.blogapplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(User author);
    List<Post> findByTitleContaining(String keyword);

    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByAuthorOrderByCreatedAtDesc(User author);
    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.id = :id")
    Optional<Post> findByIdWithAuthor(@Param("id") Long id);

}