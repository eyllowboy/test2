package com.example.library.library.controller;

import com.example.library.library.model.Article;
import com.example.library.library.model.User;
import com.example.library.library.service.ArticleService;
import com.example.library.library.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/articles")
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Autowired
    UserService userService;

    @GetMapping("")


    public String articles(Model model, HttpServletRequest request) {

        String userName = request.getRemoteUser();
        Optional<User> optional = userService.getUserByLogin(userName);
        User user = optional.orElseThrow(()-> new ServiceException("Warning  Error"));


        model.addAttribute("articles", articleService.getMyArticles(user.getLogin()));

        return "articles/article";
    }
    @GetMapping("/addArticle")
    public String addUser(Model model) {
        try {
            model.addAttribute("article", new Article());

            return "articles/modal/addArticle";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "articles/modal/addArticle";
        }
    }

    @PostMapping(value ="/saveArticle", consumes = {"multipart/form-data"})
    public String saveArticle(Article article, Model model, HttpServletRequest request, @RequestParam("maintenanceFile") MultipartFile maintenanceFile) {


        String userName = request.getRemoteUser();
        Optional<User> optional = userService.getUserByLogin(userName);
        User user = optional.orElseThrow(()-> new ServiceException("Warning  Error"));
        article.setUser(user);
        LocalDate dateCreated = LocalDate.now();
        article.setDataArticle(dateCreated);

        //******************************************************
        try {




            articleService.createNewArticle(article, maintenanceFile);
            model.addAttribute("articles", articleService.getMyArticles(userName));
            model.addAttribute("message", "???????????? ?????????????? ??????????????????");
            model.addAttribute("alertClass", "alert-success");
            return "articles/article :: article_list";
        } catch (Exception e) {
            model.addAttribute("articles", articleService.getMyArticles(userName));
            model.addAttribute("message", "???????????? ???????????????????? ????????????");
            model.addAttribute("alertClass", "alert-danger");
            return "articles/article :: article_list";
        }
    }
    @RequestMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)

    public ResponseEntity<byte[]> getImage(Long pid) throws IOException {
        Article article = articleService.getArticleById(pid);
        byte[] imageContent = null;
        imageContent = article.getCover();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteArticle(Long pid, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUser = auth.getName();

        try {
            articleService.deleteArticleById(pid);
            model.addAttribute("articles", articleService.getMyArticles(authUser));
            model.addAttribute("message", "???????????? ?????????????? ????????????");
            model.addAttribute("alertClass", "alert-success");
            return "articles/article :: article_list";
        } catch (Exception e) {
            model.addAttribute("articles", articleService.getMyArticles(authUser));
            model.addAttribute("message", "???????????? ???????????????? ????????????");
            model.addAttribute("alertClass", "alert-danger");
            return "articles/article :: article_list";
        }
    }

    @GetMapping("/edit")
    public String editArticle(Long pid, Model model) {


        try {
            Article article = articleService.getArticleById(pid);

            model.addAttribute("article", article);
            return "articles/modal/editArticle";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "articles/modal/editArticle";
        }
    }


    @PostMapping(value ="/updateArticle", consumes = {"multipart/form-data"})
    public String updateArticle(Article article, Model model, HttpServletRequest request, @RequestParam("maintenanceFile") MultipartFile maintenanceFile) {
        String userName = request.getRemoteUser();
        Optional<User> optional = userService.getUserByLogin(userName);
        User user = optional.orElseThrow(()-> new ServiceException("Warning  Error"));
        article.setUser(user);
        System.out.println(user.getLogin());
        try {
            articleService.updateArticle(article, maintenanceFile);
            model.addAttribute("articles", articleService.getMyArticles(userName));
            model.addAttribute("message", "???????????? ?????????????? ??????????????????????????");
            model.addAttribute("alertClass", "alert-success");
            return "articles/article :: article_list";
        } catch (Exception e) {
            model.addAttribute("articles", articleService.getMyArticles(userName));
            model.addAttribute("message", "???????????? ???????????????????????????? ????????????");
            model.addAttribute("alertClass", "alert-danger");
            return "articles/article :: article_list";
        }
    }
//
//    @GetMapping("/filter")
//    public String filterArticle(String filterText, Model model) {
//        List<Article> filterArticle;
//        try {
//            if (!StringUtils.isBlank(filterText)) {
//                filterArticle = articleService.filterArticle(filterText);
//            } else {
//                filterArticle = articleService.getAllArticles();
//            }
//            model.addAttribute("articles", filterArticle);
//            return "allarticles/article :: article_list";
//        } catch (Exception e) {
//            model.addAttribute("users", articleService.getAllArticles());
//            model.addAttribute("message", "?????? ???????????? ???? ???????????????? ?????????????????? ????????????");
//            model.addAttribute("alertClass", "alert-danger");
//            return "allarticles/article :: article_list";
//        }
//    }

}
