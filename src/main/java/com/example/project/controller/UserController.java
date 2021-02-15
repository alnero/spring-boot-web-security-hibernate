package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.model.Role;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String showUser(@RequestParam Long id, ModelMap model, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        Long authenticatedUserId = authenticatedUser.getId();
        String authenticatedUserRole = authenticatedUser.getAuthorities().iterator().next().getAuthority();
        // admin has access to any user page
        if (Role.AvailableRoles.ADMIN.name().equals(authenticatedUserRole) && !id.equals(authenticatedUserId)) {
            User user = userService.getById(id);
            model.addAttribute("user", user);
            return "user";
        }
        // user has access only to his own page and re-directed to his own page when trying to access pages of other users
        if (id.equals(authenticatedUserId)) {
            model.addAttribute("user", authenticatedUser);
            return "user";
        } else {
            return "redirect:/user?id=" + authenticatedUserId;
        }
    }
}
