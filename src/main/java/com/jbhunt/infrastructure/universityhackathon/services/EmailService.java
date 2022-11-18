package com.jbhunt.infrastructure.universityhackathon.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailService {

    private final SendGrid sendGrid;

    private static final String APPLICATION_EMAIL = "hackateam@jbhackathon.com";

    public void sendEmail(String recipientEmail, String templateID, Map<String, String> templateData) {
        Mail mail = createMail(recipientEmail, templateID, templateData);
        send(mail);
        log.info("Email sent to " + recipientEmail);
    }

    private void send(Mail mail){
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            this.sendGrid.api(request);
            log.info("Email was sent successfully");
        } catch (IOException exception) {
            log.error(exception.getMessage());
        }
    }

    private Mail createMail(String recipientEmail, String templateID, Map<String, String> templateData) {
        Email from = new Email(APPLICATION_EMAIL);
        Email to = new Email(recipientEmail);
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTemplateId(templateID);
        var personalization = new Personalization();
        personalization.addTo(to);
        templateData.forEach(personalization::addDynamicTemplateData);
        mail.addPersonalization(personalization);

        return mail;
    }
}

