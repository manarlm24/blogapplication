package org.example.blogapplication.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // returns dashboard.html
    }
}

