package com.example.sweater.domain.dto;

public class UserSaleDto {
    private Long id;
    private Long userId;
    private String username;
    private Long messageId;
    private String messageTitle;
    private long cost;

    public UserSaleDto() {
    }

    public UserSaleDto(Long id, Long userId, String username, Long messageId, String messageTitle, long cost) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.messageId = messageId;
        this.messageTitle = messageTitle;
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}