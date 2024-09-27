package com.example.sweater.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Please fill the message")
    @Length(max=2048, message = "Message too long")
    private String text;
    @Length(max=255, message = "Tag too long")
    private String tag;

    @Length(max=2048, message = "Message too long")
    private String title;

    @Length(max=2048, message = "Message too long")
    private String full_text;

    private Long cost;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "messageReview", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "message", fetch = FetchType.EAGER)
    private Set<UserSale> userSales = new HashSet<>();

    private String filename;
    public Message(){}
    public Message(String text, String tag, User user, String title, String full_text, long cost) {
        this.author = user;
        this.text = text;
        this.title = title;
        this.tag = tag;
        this.full_text = full_text;
        this.cost = cost;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getAuthorName(){
        return author !=null ? author.getUsername() : "<none>";
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Set<UserSale> getUserSales() {
        return userSales;
    }

    public void setUserSales(Set<UserSale> userSales) {
        this.userSales = userSales;
    }
}
