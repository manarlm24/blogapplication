package org.example.blogapplication.models;

import jakarta.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ElementCollection(fetch = FetchType.EAGER) // Stores roles as a set of strings in a separate table
    @Enumerated(EnumType.STRING) // Store the enum as a string in the database
    @CollectionTable(name =
            "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<Role> roles;

    // Constructors
    public User() {}
    public User(String username, String email, String password, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getUsername() { return username; }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public List<Post> getPosts() { return posts; }

    public List<Comment> getComments() { return comments; }

    public Set<Role> getRoles() { return roles; }  // Get roles

    public void setId(Long id) { this.id = id; }

    public void setUsername(String username) { this.username = username; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setPosts(List<Post> posts) { this.posts = posts; }

    public void setComments(List<Comment> comments) { this.comments = comments; }

    public void setRoles(Set<Role> roles) { this.roles = roles; }  // Set roles
}
