package com.example.sweater.domain;


import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
public class Review implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Пожалуйста заполните текст отзыва")
    @Length(max = 2048, message = "Отзыв слишком длинный")
    private String review_text;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id_review")
    private User author_review;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "message_id_review")
    private Message messageReview;
    public Review() {}
    public Review(String review_text, User user, Message message) {
        this.author_review = user;
        this.review_text = review_text;
        this.messageReview = message;
    }

    // Геттеры и сеттеры
    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor_review() {
        return author_review;
    }

    public void setAuthor_review(User author_review) {
        this.author_review = author_review;
    }

    public Message getMessageReview() {
        return messageReview;
    }

    public void setMessageReview(Message messageReview) {
        this.messageReview = messageReview;
    }
}
