package me.workhive.workhive.services;

public interface EmailService {

    void sendHtmlEmail(String to, String subject, String text);

}