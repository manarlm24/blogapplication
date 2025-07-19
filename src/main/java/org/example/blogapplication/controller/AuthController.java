package org.example.blogapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    // Show authentication options (Sign Up / Sign In)
    @GetMapping("/auth")
    public String showAuthOptions() {
        return "auth"; // points to auth.html
    }

}
