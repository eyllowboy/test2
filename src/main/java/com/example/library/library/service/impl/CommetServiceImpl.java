package com.example.library.library.service.impl;

import com.example.library.library.model.Comment;
import com.example.library.library.model.Message;
import com.example.library.library.model.User;
import com.example.library.library.repository.CommentRepository;
import com.example.library.library.repository.MessageRepository;
import com.example.library.library.service.CommentService;
import com.example.library.library.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommetServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Override
    public Comment createNewComment(Comment comment) {
        return commentRepository.save(comment);
    }


}
