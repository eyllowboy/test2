package com.example.library.library.controller;

import com.example.library.library.model.Article;
import com.example.library.library.model.Status;
import com.example.library.library.model.User;
import com.example.library.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    UserService userService;
    @GetMapping("/login")
    private String getLoginPage() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("message", "Неверный логин или пароль");
        model.addAttribute("alertClass", "alert-danger");
        return "login";
    }
    @GetMapping("/registration")
    public String registration(Model model) {
        try {
            model.addAttribute("article", new Article());

            return "registration/registration";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "registration/registration";
        }
    }
    @PostMapping("/saveUser")
    public String saveUser(User user, Model model) {
        Status status = Status.ACTIVE;
        user.setStatus(status);
        LocalDate createdDate = LocalDate.now();
        user.setDataCreated(createdDate);
//        LocalDateTime dateVisited = LocalDateTime.now();
//        user.setDataVisited(dateVisited);
        try {
            userService.createNewUser(user);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("message", "Пользователь успешно создан");
            model.addAttribute("alertClass", "alert-success");
            return "login";
        } catch (Exception e) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("message", "Ошибка создания пользователя");
            model.addAttribute("alertClass", "alert-danger");
            return "login";
        }
    }
}
