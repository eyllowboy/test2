package com.example.library.library.service;


import com.example.library.library.model.Message;
import com.example.library.library.model.User;

import java.util.List;


public interface MessageService {

    Message createNewMessage(Message message);

    List<Message> getMessages(User user);

    Message getOneMessage(Long pid);

    void deleteMessageById(Long pid);
}
