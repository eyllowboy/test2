package com.example.library.library.repository;

import com.example.library.library.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a  where a.authUser = :auth")
    List<Article> findMyArticle(@Param("auth")String Auth);
}
