package com.example.sweater.domain.dto;

import java.util.List;

public class MessageDto {

    private Long id;
    private String text;
    private String title;
    private String fullText;
    private String tag;
    private Long cost;
    private String authorName;
    private List<String> reviews;
    private String filename;

    public MessageDto() {
    }

    public MessageDto(Long id, String text, String title, String fullText, String tag, Long cost, String authorName, List<String> reviews, String filename) {
        this.id = id;
        this.text = text;
        this.title = title;
        this.fullText = fullText;
        this.tag = tag;
        this.cost = cost;
        this.authorName = authorName;
        this.reviews = reviews;
        this.filename = filename;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}

