package com.example.sweater.domain.dto;

public class ReviewDto {
    private Long id;
    private String reviewText;
    private String authorName;
    private Long messageId;

    public ReviewDto() {
    }

    public ReviewDto(Long id, String reviewText, String authorName, Long messageId) {
        this.id = id;
        this.reviewText = reviewText;
        this.authorName = authorName;
        this.messageId = messageId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}
