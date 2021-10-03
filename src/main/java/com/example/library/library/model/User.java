package com.example.library.library.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    @Column(nullable = false, length = 100, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(length = 100)
    private String middleName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles")
    private List<Role> roles = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) default 'ACTIVE'")
    private Status status;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Article> articles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Message> messages;


    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "dataCreated")
    private LocalDate dataCreated;

    @DateTimeFormat (pattern = ("yyyy-MM-dd HH:mm"))
    @Column(name = "dataVisited")
    private LocalDateTime dataVisited;

    @ManyToMany(mappedBy = "userLike", fetch = FetchType.LAZY)
    private List<Article> articlesLike = new ArrayList<>();

    public LocalDateTime getDataVisited() {
        return dataVisited;
    }

    public void setDataVisited(LocalDateTime dataVisited) {
        this.dataVisited = dataVisited;
    }

    public LocalDate getDataCreated() {
        return dataCreated;
    }

    public void setDataCreated(LocalDate dataCreated) {
        this.dataCreated = dataCreated;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Article> getArticlesLike() {
        return articlesLike;
    }

    public void setArticlesLike(List<Article> articlesLike) {
        this.articlesLike = articlesLike;
    }

    @Transient
    public String getRolesStr() {
        return roles.stream().map(Role::getName).collect(Collectors.joining(","));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return pid.equals(user.pid) &&
                login.equals(user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, login);
    }
}
