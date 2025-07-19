package org.example.blogapplication.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(
            name = "author_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_comment_author",
                    foreignKeyDefinition = "FOREIGN KEY (author_id) REFERENCES app_user(id) ON DELETE CASCADE")
    )
    private User author;

    @ManyToOne
    private Post post;

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
}

