package com.example.library.library.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    @Column(nullable = false, length = 100)
    private String themeMessage;

    @Column(nullable = false, length = 2000)
    private String textMessage;


    @DateTimeFormat (pattern = ("yyyy-MM-dd HH:mm"))
    @Column(name = "dataMessage")
    private LocalDateTime dataMessage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_message", referencedColumnName = "pid")
    private User user;

    @Column(nullable = false, length = 100)
    private String fromUser;


    public Message() {
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getThemeMessage() {
        return themeMessage;
    }

    public void setThemeMessage(String themeMessage) {
        this.themeMessage = themeMessage;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public LocalDateTime getDataMessage() {
        return dataMessage;
    }

    public void setDataMessage(LocalDateTime dataMessage) {
        this.dataMessage = dataMessage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }
}
