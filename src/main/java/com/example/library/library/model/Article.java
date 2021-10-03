package com.example.library.library.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 2000)
    private String text;

    @Column(  length = 2000, columnDefinition = "false")
    private boolean isLiked =false;



    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dataArticle")
    private LocalDate dataArticle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_name", referencedColumnName = "pid")
    private User user;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "user_likes")
    private List<User> userLike = new ArrayList<>();

    @Lob
    private byte[] cover;

    public Article() {
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Transient
    public String getAuthorStr() {
        return user.getLogin();
    }

    @Transient
    public List<String> ComText() {
        return comments.stream().map(Comment::getCommentText).collect(Collectors.toList());

    }
    @Transient
    public int allNumberLikes() {
        return userLike.size();

    }
    @Transient
    public Boolean ArticleIsLiked(User user) {
        return userLike.contains(user);

    }


    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public LocalDate getDataArticle() {
        return dataArticle;
    }

    public void setDataArticle(LocalDate dataArticle) {
        this.dataArticle = dataArticle;
    }

    public List<User> getUserLike() {
        return userLike;
    }

    public void setUserLike(List<User> userLike) {
        this.userLike = userLike;
    }
}
