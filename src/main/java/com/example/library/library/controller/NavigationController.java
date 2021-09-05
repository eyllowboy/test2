package com.example.library.library.controller;

import com.example.library.library.model.Article;
import com.example.library.library.model.Message;
import com.example.library.library.model.User;
import com.example.library.library.service.ArticleService;
import com.example.library.library.service.BookService;
import com.example.library.library.service.MessageService;
import com.example.library.library.service.UserService;

import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.Optional;

@Controller
public class NavigationController {

    private ArticleService articleService;

    private UserService userService;

    private BookService bookService;

    private MessageService messageService;

    public NavigationController(UserService userService, BookService bookService, ArticleService articleService,
                                MessageService messageService) {
        this.userService = userService;
        this.bookService = bookService;
        this.articleService = articleService;
        this.messageService = messageService;
    }

    @RequestMapping(value = {"/", "/users"}, method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN')")
    public String users(Model model, HttpServletRequest request) {
        String userName = request.getRemoteUser();
        Optional<User> optional = userService.getUserByLogin(userName);
        User user = optional.orElseThrow(()-> new ServiceException("Warning  Error"));


        LocalDateTime dateVisited = LocalDateTime.now();
        user.setDataVisited(dateVisited);

        userService.updateParametredUser(user);
        model.addAttribute("users", userService.getAllUsers());
        return "users/user";
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

    public String allArticles(Model model, HttpServletRequest request) {
        String userName = request.getRemoteUser();
        Optional<User> optional = userService.getUserByLogin(userName);
        User user = optional.orElseThrow(()-> new ServiceException("Warning  Error"));


        LocalDateTime dateVisited = LocalDateTime.now();
        user.setDataVisited(dateVisited);

        userService.updateParametredUser(user);
        model.addAttribute("articles", articleService.getAllArticles());

        return "allarticles/article";
    }

    @GetMapping("/allarticles/filter")


    public String filterArticle(String filterText, Model model) {
        List<Article> filterArticle;
        try {
            if (!StringUtils.isBlank(filterText)) {

                filterArticle = articleService.filterArticle(filterText);

            } else {
                filterArticle = articleService.getAllArticles();
            }
            model.addAttribute("articles", filterArticle);
            return "allarticles/article :: article_list";
        } catch (Exception e) {
            model.addAttribute("users", articleService.getAllArticles());
            model.addAttribute("message", "При работе со статьями произошла ошибка");
            model.addAttribute("alertClass", "alert-danger");
            return "allarticles/article :: article_list";
        }
    }


//    @GetMapping("/index")
//    @PreAuthorize("hasAuthority('USER')")
//    public String getBooksForUser(Model model) {
//        model.addAttribute("books", bookService.getAllBooks());
//        return "index";
//    }
}
