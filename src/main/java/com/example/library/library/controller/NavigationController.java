package com.example.library.library.controller;

import com.example.library.library.model.User;
import com.example.library.library.service.ArticleService;
import com.example.library.library.service.BookService;
import com.example.library.library.service.UserService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class NavigationController {

    private ArticleService articleService;

    private UserService userService;

    private BookService bookService;

    public NavigationController(UserService userService, BookService bookService, ArticleService articleService) {
        this.userService = userService;
        this.bookService = bookService;
        this.articleService = articleService;
    }

    @RequestMapping(value = {"/", "/users"}, method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String users(Model model, HttpServletRequest request) {
        String userName = request.getRemoteUser();
        Optional<User> optional = userService.getUserByLogin(userName);
        User user = optional.orElseThrow(()-> new ServiceException("Warning  Error"));


        LocalDateTime dateVisited = LocalDateTime.now();
        user.setDataVisited(dateVisited);
        System.out.println("????????????????????????????????????????????");
        System.out.println(userName);
        userService.updateVisitedUser(user);
        model.addAttribute("users", userService.getAllUsers());
        return "users/user";
    }

    @GetMapping("/references")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LIBRARIAN')")
    public String references() {
        return "references/references";
    }

    @GetMapping("/articles")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")

    public String articles(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authName = auth.getName();

        model.addAttribute("articles", articleService.getMyArticles(authName));
        //model.addAttribute("authName", authName);

        return "articles/article";
    }

    @GetMapping("/allarticles")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")

    public String allArticles(Model model) {

        model.addAttribute("articles", articleService.getAllArticles());

        return "allarticles/article";
    }

    @GetMapping("/index")
    @PreAuthorize("hasAuthority('USER')")
    public String getBooksForUser(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "index";
    }
}
