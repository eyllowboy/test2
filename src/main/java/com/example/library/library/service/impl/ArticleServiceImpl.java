package com.example.library.library.service.impl;

import com.example.library.library.model.Article;
import com.example.library.library.repository.ArticleRepository;
import com.example.library.library.service.ArticleService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class ArticleServiceImpl  implements ArticleService {

    @Autowired
    ArticleRepository articleRepository;


    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }
    @Override
    public List<Article> getMyArticles(String Auth) {
        return articleRepository.findMyArticle(Auth);
    }


    @Override
    public Article createNewArticle(Article article, MultipartFile maintenanceFile) throws ServiceException {
        if (!maintenanceFile.isEmpty()) {
            try {
                byte[] fileBytes = maintenanceFile.getBytes();
                article.setCover(fileBytes);
            } catch (IOException e) {
                throw new ServiceException("Ошибка добавления обложки");
            }
        }
        return articleRepository.save(article);
    }

    @Override
    public Article getArticleById(Long pid) {
        return articleRepository.getOne(pid);
    }


    @Override
    @Transactional
    public void deleteArticleById(Long pid) {
        articleRepository.deleteById(pid);
    }

    @Override
    @Transactional
    public Article updateArticle(Article article, MultipartFile maintenanceFile) throws ServiceException {
        if (!maintenanceFile.isEmpty()) {
            try {
                byte[] fileBytes = maintenanceFile.getBytes();
                article.setCover(fileBytes);
            } catch (IOException e) {
                throw new ServiceException("Ошибка добавления обложки");
            }
        }
        return articleRepository.save(article);
    }

    @Override
    public List<Article> summaArticles(Long pid) {
        return articleRepository.summaArticles(pid);
    }
}
