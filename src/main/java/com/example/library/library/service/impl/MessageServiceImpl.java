package com.example.library.library.service.impl;

import com.example.library.library.model.Message;
import com.example.library.library.model.User;
import com.example.library.library.repository.MessageRepository;
import com.example.library.library.service.MessageService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Override
    public Message createNewMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessages(User user) {
        return messageRepository.findbyMessage(user);
    }

    @Override
    public Message getOneMessage(Long pid) {
        return messageRepository.getOne(pid);
    }

    @Override
    public void deleteMessageById(Long pid) {
        messageRepository.deleteById(pid);
    }
}
