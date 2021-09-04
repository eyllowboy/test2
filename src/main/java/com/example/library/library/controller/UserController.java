package com.example.library.library.controller;

import com.example.library.library.model.Message;
import com.example.library.library.model.User;
import com.example.library.library.service.ArticleService;
import com.example.library.library.service.MessageService;
import com.example.library.library.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final String USER_TABLE = "users/user :: user_list";
    private static final String ERROR_ALERT = "fragments/alert :: alert";
    private static final String EDIT_MODAL = "users/modal/editUser";
    private static final String ADD_MODAL = "users/modal/addUser";
    private static final String USER_INFO = "users/modal/infoUser";
    private static final String MESSAGE_USER = "users/modal/messageUser";

    @Autowired
    ArticleService articleService;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/addUser")
    public String addUser(Model model) {
        try {
            model.addAttribute("predefinedRoles", userService.getAllRoles());
            model.addAttribute("user", new User());
            return ADD_MODAL;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ADD_MODAL;
        }
    }

    @GetMapping("/checkLogin")
    public String checkLogin(@RequestParam("login") String login, Model model, HttpServletResponse response) {
        if (userService.getUserByLogin(login).isPresent()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            model.addAttribute("message", "Пользователь с таким логином уже существует");
            model.addAttribute("alertClass", "alert-danger");
        }
        return ERROR_ALERT;
    }

    @PostMapping("/saveUser")
    public String saveUser(User user, Model model) {
        try {
            userService.createNewUser(user);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("message", "Пользователь успешно добавлен");
            model.addAttribute("alertClass", "alert-success");
            return USER_TABLE;
        } catch (Exception e) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("message", "Ошибка добавления пользователя");
            model.addAttribute("alertClass", "alert-danger");
            return USER_TABLE;
        }
    }

    private boolean checkUserLoginUnique(String login) {
        return !userService.getUserByLogin(login).isPresent();
    }

    @GetMapping("/edit")
    public String editUser(Long pid, Model model) {
        try {
            User user = userService.getUserById(pid);
            model.addAttribute("predefinedRoles", userService.getAllRoles());
            model.addAttribute("user", user);
            return EDIT_MODAL;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return EDIT_MODAL;
        }
    }
    @GetMapping("/message")
    public String messageUser(Long pid, Model model) {

        try {
            User user = userService.getUserById(pid);

            model.addAttribute("user", user);

            return MESSAGE_USER;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return MESSAGE_USER;
        }
    }
    @PostMapping("/updateUser")
    public String updateUser(User user, Model model) {
        try {
            userService.updateUser(user);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("message", "Пользователь успешно изменен");
            model.addAttribute("alertClass", "alert-success");
            return USER_TABLE;
        } catch (Exception e) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("message", "Ошибка редактирования пользователя");
            model.addAttribute("alertClass", "alert-danger");
            return USER_TABLE;
        }
    }
    @GetMapping(value = "/info")
    public String infoUser(Long pid, Model model) {
        try {

            model.addAttribute("user", userService.getUserById(pid));
            model.addAttribute("countArticles", articleService.summaArticles(pid).size());


            return USER_INFO;
        } catch (Exception e) {
            System.err.println(e.getMessage());

            return USER_INFO;
        }
    }
//    @GetMapping(value = "/message")
//    public String messageUser(Long pid, Model model) {
//        try {
//
//            model.addAttribute("user", userService.getUserById(pid));
//            model.addAttribute("countArticles", articleService.summaArticles(pid).size());
//
//
//            return MESSAGE_USER;
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//
//            return MESSAGE_USER;
//        }
//    }
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteUser(Long pid, Model model) {
        try {
            userService.deleteUserById(pid);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("message", "Пользователь успешно удален");
            model.addAttribute("alertClass", "alert-success");
            return USER_TABLE;
        } catch (Exception e) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("message", "Ошибка удаления пользователя");
            model.addAttribute("alertClass", "alert-danger");
            return USER_TABLE;
        }
    }

    @GetMapping("/filter")
    public String filterUser(String filterText, Model model) {
        List<User> filterUsers;
        try {
            if (!StringUtils.isBlank(filterText)) {
                filterUsers = userService.filterUser(filterText);
            } else {
                filterUsers = userService.getAllUsers();
            }
            model.addAttribute("users", filterUsers);
            return USER_TABLE;
        } catch (Exception e) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("message", "При работе с пользователями произошла ошибка");
            model.addAttribute("alertClass", "alert-danger");
            return USER_TABLE;
        }
    }
    @PostMapping("/saveMessage")
    public String saveMessage(@RequestParam(value="pid") Long pid,
                              Message message, HttpServletRequest request, Model model) {
        try {
            String userName = request.getRemoteUser();
            Optional<User> optional = userService.getUserByLogin(userName);
            User fromUser = optional.orElseThrow(()-> new ServiceException("Warning  Error"));
           message.setFromUser(fromUser.getLogin());

            User user = userService.getUserById(pid);
            message.setUser(user);
            LocalDateTime dataMessage = LocalDateTime.now();
            message.setDataMessage(dataMessage);
            messageService.createNewMessage(message);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("message", "Сообщение успешно отправлено");
            model.addAttribute("alertClass", "alert-success");
            return USER_TABLE;
        } catch (Exception e) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("message", "Ошибка добавления сообщения");
            model.addAttribute("alertClass", "alert-danger");
            return USER_TABLE;
        }
    }
}
