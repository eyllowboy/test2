package com.example.library.library.repository;

import com.example.library.library.model.Article;
import com.example.library.library.model.Message;
import com.example.library.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m  where m.user=:user")
    List<Message> findbyMessage(@Param("user") User user);
}
