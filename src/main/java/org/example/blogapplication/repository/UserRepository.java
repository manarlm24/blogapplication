package org.example.blogapplication.repository;


import org.example.blogapplication.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByUsername(String username);
        Optional<User> findByEmail(String email);

    }





