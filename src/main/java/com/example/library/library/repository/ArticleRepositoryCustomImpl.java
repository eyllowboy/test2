package com.example.library.library.repository;

import com.example.library.library.model.Article;
import com.example.library.library.model.Book;
import com.example.library.library.model.User;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Article> filterArticle(String filterStr) {
        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Article.class);
        criteria.createAlias("user", "user");
        criteria.setFetchMode("user", FetchMode.JOIN);

        Disjunction disjunction = Restrictions.or(
                Restrictions.ilike("user.login", filterStr, MatchMode.ANYWHERE),
                Restrictions.ilike("text", filterStr, MatchMode.ANYWHERE),
                Restrictions.ilike("name", filterStr, MatchMode.ANYWHERE)

        );
        criteria.add(disjunction);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

}
