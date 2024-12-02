package com.example.sweater.service;

public interface MailSenderInterface {
    public void send(String emailTo, String subject, String message);
}
