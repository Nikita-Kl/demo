package com.example.sweater.domain.dto;

import java.util.Set;

public class UserDto {
    private Long id;
    private String username;
    private String email;
    private boolean active;
    private Set<String> roles;
    private Set<Long> messageIds;
    private Set<Long> reviewIds;

    public UserDto() {
    }

    public UserDto(Long id, String username, String email, boolean active, Set<String> roles, Set<Long> messageIds, Set<Long> reviewIds) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.active = active;
        this.roles = roles;
        this.messageIds = messageIds;
        this.reviewIds = reviewIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<Long> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(Set<Long> messageIds) {
        this.messageIds = messageIds;
    }

    public Set<Long> getReviewIds() {
        return reviewIds;
    }

    public void setReviewIds(Set<Long> reviewIds) {
        this.reviewIds = reviewIds;
    }
}