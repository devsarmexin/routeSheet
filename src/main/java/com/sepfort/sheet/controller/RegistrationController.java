package com.sepfort.sheet.controller;

import com.sepfort.sheet.domain.Role;
import com.sepfort.sheet.domain.User;
import com.sepfort.sheet.repo.UserRepo;
import com.sepfort.sheet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        return userService.addUser(user, model);
    }
}
