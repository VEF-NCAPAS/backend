package me.workhive.workhive.services;

import java.time.LocalDateTime;

public interface EmailTemplateService {

    String appliedTemplate(String candidateName, String vacancyTitle, String companyName);
    String reviewedTemplate(String candidateName, String vacancyTitle);
    String interviewTemplate( String candidateName,  String vacancyTitle,  LocalDateTime interviewDate, String meetingLink);
    String technicalTestTemplate(String candidateName, String vacancyTitle, String testLink, LocalDateTime deadline);
    String selectedTemplate(String candidateName, String vacancyTitle);
    String rejectedTemplate(String candidateName, String vacancyTitle);
    String withdrawnTemplate(String candidateName, String vacancyTitle);
}