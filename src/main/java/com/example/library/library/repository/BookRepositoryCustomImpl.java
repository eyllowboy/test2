package com.example.library.library.repository;


import com.example.library.library.model.Book;
import com.example.library.library.model.User;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> filterUser(String filterStr) {
        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Book.class);
        criteria.createAlias("author", "author");
        criteria.setFetchMode("author", FetchMode.JOIN);
        LogicalExpression disjunction = Restrictions.or(
                Restrictions.ilike("name", filterStr, MatchMode.ANYWHERE),
                Restrictions.ilike("author.lastName", filterStr, MatchMode.ANYWHERE)
        );
        criteria.add(disjunction);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }
}
