package com.thesis.backend.service.user.impl;

import com.thesis.backend.service.user.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendHtml(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true = HTML

        mailSender.send(message);
    }

    public String loadEmailTemplate(String path, String link) {
        try {
            InputStream inputStream = new ClassPathResource("templates/" + path).getInputStream();
            String html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return html.replace("{{VERIFICATION_LINK}}", link);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }

    public void sendVerificationEmail(String to, String verificationLink) throws MessagingException {
        String subject = "Complete Your Registration";
        String content = loadEmailTemplate("verification-email.html", verificationLink);
        sendHtml(to, subject, content);
    }

    public void sendInvoiceEmail(String to, String subject, String body, byte[] pdfAttachment, String filename) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        if (pdfAttachment != null) {
            ByteArrayDataSource dataSource = new ByteArrayDataSource(pdfAttachment, "application/pdf");
            helper.addAttachment(filename, dataSource);
        }

        mailSender.send(message);
    }

}
