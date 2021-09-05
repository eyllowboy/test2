package com.example.library.library.service;

import com.example.library.library.model.Article;
import com.example.library.library.model.Book;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface ArticleService {
    List<Article> getAllArticles();

   Article createNewArticle(Article article, MultipartFile maintenanceFile);

    Article getArticleById(Long pid);

    void deleteArticleById(Long pid);

    Article updateArticle(Article article, MultipartFile maintenanceFile);

    List<Article> getMyArticles(String Auth);

    List<Article> summaArticles(Long pid);

    List<Article> filterArticle(String filterText);
}
