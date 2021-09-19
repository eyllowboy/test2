package com.example.library.library.repository;

import com.example.library.library.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {



    @Query("select a from Article a join fetch a.user where a.user.login = :auth")
    List<Article> findMyArticle(@Param("auth")String Auth);

    @Query("select a from Article a join fetch a.user where a.user.pid = :pid")
    List<Article> summaArticles(Long pid);

    @Query("select a from Article a where a.dataArticle =:localDate")
    List<Article>allArticlesToday(LocalDate localDate);

}
