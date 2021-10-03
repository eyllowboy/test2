package com.example.library.library.controller;

import com.example.library.library.model.Article;
import com.example.library.library.model.Comment;
import com.example.library.library.model.Message;
import com.example.library.library.model.User;
import com.example.library.library.service.*;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
public class AllarticleController {

    private ArticleService articleService;

    private UserService userService;

    private MessageService messageService;

    private CommentService commentService;

    public AllarticleController(UserService userService, ArticleService articleService,
                                MessageService messageService, CommentService commentService) {
        this.userService = userService;

        this.articleService = articleService;
        this.messageService = messageService;
        this.commentService = commentService;
    }


    @GetMapping("/allarticles")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")

    public String allArticles(Model model, HttpServletRequest request) {
        try {
            String userName = request.getRemoteUser();
            Optional<User> optional = userService.getUserByLogin(userName);
            User user = optional.orElseThrow(() -> new ServiceException("Warning  Error"));


            LocalDateTime dateVisited = LocalDateTime.now();
            user.setDataVisited(dateVisited);

            userService.updateParametredUser(user);

            List<Article> list =articleService.getAllArticles();
            for(Article article1: list){
                if(article1.getUserLike().contains(user)){
                    article1.setLiked(true);
                    articleService.updateArticle(article1);
                }
                else {article1.setLiked(false);
                    articleService.updateArticle(article1);
                }
            }

            model.addAttribute("articles", articleService.getAllArticles());

            return "allarticles/article";
        }catch (Exception e){

            model.addAttribute("articles", articleService.getAllArticles());

            return "allarticles/article";
        }
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
            model.addAttribute("articles", articleService.getAllArticles());
            model.addAttribute("message", "При работе со статьями произошла ошибка");
            model.addAttribute("alertClass", "alert-danger");
            return "allarticles/article :: article_list";
        }
    }

    @GetMapping("/allarticles/today")
    public String filterArticle(Model model) {
        List<Article> allArticles;
        try {
            allArticles = articleService.allArticlesToday();
            model.addAttribute("articles", allArticles);
            return "allarticles/article :: article_list";
        } catch (Exception e) {
            model.addAttribute("articles", articleService.getAllArticles());
            model.addAttribute("message", "При работе со статьями произошла ошибка");
            model.addAttribute("alertClass", "alert-danger");
            return "allarticles/article :: article_list";
        }
    }


    @RequestMapping(value = "/allarticles/delete", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteArticle(Long pid, HttpServletRequest request, Model model) {

        try {
            String userName = request.getRemoteUser();
            Optional<User> optional = userService.getUserByLogin(userName);
            User fromUser = optional.orElseThrow(() -> new ServiceException("Warning  Error not found user"));
            Message message = new Message();
            message.setFromUser(fromUser.getLogin());
            Article article = articleService.getArticleById(pid);
            User user = article.getUser();


            message.setUser(user);
            message.setThemeMessage("Удаление вашей статьи");
            message.setTextMessage("Ваша статьи " + article.getName() + " была удалена администратором " + user.getLogin());

            LocalDateTime dataMessage = LocalDateTime.now();
            message.setDataMessage(dataMessage);
            messageService.createNewMessage(message);


            articleService.deleteArticleById(pid);
            model.addAttribute("articles", articleService.getAllArticles());
            model.addAttribute("message", "Статья успешно удален");
            model.addAttribute("alertClass", "alert-success");
            return "allarticles/article :: article_list";
        } catch (Exception e) {
            model.addAttribute("articles", articleService.getAllArticles());
            model.addAttribute("message", "Ошибка удаления статьи");
            model.addAttribute("alertClass", "alert-danger");
            return "allarticles/article :: article_list";
        }
    }

    @GetMapping("/allarticles/comment")
    public String commentUser(Long pid, Model model) {

        try {
            Article article = articleService.getArticleById(pid);

            model.addAttribute("article", article);

            return "allarticles/modal/commentArticle";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "allarticles/modal/commentArticle";
        }
    }

    @PostMapping("/allarticles/saveComment")
    public String saveComment(@RequestParam(value = "pid") Long pid, @RequestParam(value = "CommentText") String CommentText,
                              HttpServletRequest request, Model model) {
        try {
            Comment comment = new Comment();
            comment.setCommentText(CommentText);
            Article article = articleService.getArticleById(pid);
            comment.setArticle(article);

            commentService.createNewComment(comment);

            model.addAttribute("articles", articleService.getAllArticles());
            model.addAttribute("message", "Комментарий успешно отправлен");
            model.addAttribute("alertClass", "alert-success");
            return "allarticles/article:: article_list";
        } catch (Exception e) {
            model.addAttribute("articles", articleService.getAllArticles());
            model.addAttribute("message", "Ошибка добавления комментария");
            model.addAttribute("alertClass", "alert-danger");
            return "allarticles/article:: article_list";
        }
    }

    @GetMapping("/allarticles/like")
    public String UserLike(@RequestParam(value = "pid")Long pid, Model model, HttpServletRequest request) {


        try {
            String userName = request.getRemoteUser();
            Optional<User> optional = userService.getUserByLogin(userName);
            User user = optional.orElseThrow(() -> new ServiceException("Warning  Error not found user"));
            Article article = articleService.getArticleById(pid);
            List<User> userList = article.getUserLike();
            userList.add(user);
            article.setUserLike(userList);
            articleService.updateArticle(article);

            List<Article> list =articleService.getAllArticles();
            for(Article article1: list){
                if(article1.getUserLike().contains(user)){
                    article1.setLiked(true);
                    articleService.updateArticle(article1);
                }
                else {article1.setLiked(false);
                    articleService.updateArticle(article1);
                }
            }
            model.addAttribute("articles",articleService.getAllArticles());

            return "allarticles/article :: article_list";
        } catch (Exception e) {
            model.addAttribute("articles", articleService.getAllArticles());

            return "allarticles/article :: article_list";
        }
    }

    @GetMapping("/allarticles/dislike")
    public String UserdisLike(@RequestParam(value = "pid")Long pid, Model model, HttpServletRequest request) {


        try {
            String userName = request.getRemoteUser();
            Optional<User> optional = userService.getUserByLogin(userName);
            User user = optional.orElseThrow(() -> new ServiceException("Warning  Error not found user"));
            Article article = articleService.getArticleById(pid);
            List<User> userList = article.getUserLike();
            userList.remove(user);
            article.setUserLike(userList);
            articleService.updateArticle(article);

            List<Article> list =articleService.getAllArticles();
            for(Article article1: list){
                if(article1.getUserLike().contains(user)){
                    article1.setLiked(true);
                    articleService.updateArticle(article1);
                }
                else {article1.setLiked(false);
                    articleService.updateArticle(article1);
                }
            }
            model.addAttribute("articles",articleService.getAllArticles());

            return "allarticles/article :: article_list";
        } catch (Exception e) {
            model.addAttribute("articles", articleService.getAllArticles());

            return "allarticles/article :: article_list";
        }
    }
}
