package com.example.library.library.controller;

import com.example.library.library.model.Article;
import com.example.library.library.model.Message;
import com.example.library.library.model.User;
import com.example.library.library.service.MessageService;
import com.example.library.library.service.UserService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/messages")
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")

    public String allmessages(Model model, HttpServletRequest request) {
        String userName = request.getRemoteUser();
        Optional<User> optional = userService.getUserByLogin(userName);
        User user = optional.orElseThrow(()-> new ServiceException("Warning  Error"));
        List<Message> list = messageService.getMessages(user);
        model.addAttribute("messages", messageService.getMessages(user));

        return "messages/message";
    }
    @GetMapping(value = "/openModal")
    public String infoUser(Long pid, Model model) {
        try {

            model.addAttribute("message", messageService.getOneMessage(pid));


            return "messages/modal/openMessage";
        } catch (Exception e) {
            System.err.println(e.getMessage());

            return "messages/modal/openMessage";
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteMessage(Long pid, Model model,HttpServletRequest request) {
        try {
            messageService.deleteMessageById(pid);
            String userName = request.getRemoteUser();
            Optional<User> optional = userService.getUserByLogin(userName);
            User user = optional.orElseThrow(()-> new ServiceException("Warning  Error"));

            model.addAttribute("messages", messageService.getMessages(user));

            model.addAttribute("message", "Сообщение успешно удалено");
            model.addAttribute("alertClass", "alert-success");
            return "messages/message :: message_list";
        } catch (Exception e) {
            String userName = request.getRemoteUser();
            Optional<User> optional = userService.getUserByLogin(userName);
            User user = optional.orElseThrow(()-> new ServiceException("Warning  Error"));
            model.addAttribute("messages", messageService.getMessages(user));
            model.addAttribute("message", "Ошибка удаления сообщения");
            model.addAttribute("alertClass", "alert-danger");
            return "messages/message :: message_list";
        }
    }
    @GetMapping("/addMessage")
    public String addMessage(Model model) {
        try {
            model.addAttribute("message", new Message());

            return "messages/modal/addMessage";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "messages/modal/addMessage";
        }
    }
}
