package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.model.Role;
import com.example.project.service.RoleService;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public String listUsers(ModelMap model) {
        List<User> users = userService.listUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/users/add")
    public String addPage(@ModelAttribute User user) {
        return "add";
    }

    @PostMapping("/users/add")
    public String add(@ModelAttribute User user, @Validated String chosenRole) {
        Set<Role> roles = roleService.getByName(chosenRole);
        user.setRoles(roles);
        userService.add(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit")
    public String editPage(@RequestParam long id, ModelMap model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "edit";
    }

    @PostMapping("/users/edit")
    public String edit(@ModelAttribute User user, @Validated String chosenRole) {
        Set<Role> roles = roleService.getByName(chosenRole);
        user.setRoles(roles);
        userService.edit(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/delete")
    public String delete(@RequestParam long id) {
        userService.delete(userService.getById(id));
        return "redirect:/admin/users";
    }
}
