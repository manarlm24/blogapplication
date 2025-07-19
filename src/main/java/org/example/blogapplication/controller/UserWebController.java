package org.example.blogapplication.controller;

import org.example.blogapplication.models.Role;
import org.example.blogapplication.models.User;
import org.example.blogapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/web/users")
public class UserWebController {

    private final UserService userService;

    @Autowired
    public UserWebController(UserService userService) {
        this.userService = userService;
    }

    // Show all users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users"; // points to users.html
    }



    // Show user creation form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "create_user"; // points to create_user.html
    }

    // Process form submission
    @PostMapping("/create")  // Changed from "/save" to match form action
    public String createUser(@ModelAttribute User user) {
        user.setRoles(Set.of(Role.USER));
        userService.createUser(user);
        return "redirect:/auth";
    }

    // Delete user
  /*  @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/web/users";
    }*/
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/web/users";
    }

    // Admin-only form to create a user with custom roles
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/create")
    public String showAdminCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("availableRoles", Role.values()); // Send all roles to the form
        return "admin_create_user"; // Points to admin_create_user.html
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public String createUserAsAdmin(@ModelAttribute User user,
                                    @RequestParam("selectedRoles") Set<Role> selectedRoles) {
        user.setRoles(selectedRoles); // Admin assigns roles
        userService.createUser(user); // createUser already encodes password
        return "redirect:/admin_create_user";
    }
  /*  // Temporary endpoint to create an admin
    @GetMapping("/create-admin-temp")
    public String createFirstAdmin() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword("admin123"); // raw password, will be encoded in service
        admin.setRoles(Set.of(Role.ADMIN)); // 👈 assign ADMIN role

        userService.createUser(admin); // Will hash the password
        return "redirect:/login"; // Or wherever your login page is}*/


}