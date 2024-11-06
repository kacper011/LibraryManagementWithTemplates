package com.example.email.email_receiver.service;

import com.example.email.email_receiver.model.Email;
import com.example.email.email_receiver.repository.EmailRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;
    @Autowired
    public EmailService(EmailRepository emailRepository, JavaMailSender javaMailSender) {
        this.emailRepository = emailRepository;
        this.javaMailSender = javaMailSender;
    }

    @RabbitListener(queues = "emailQueue")
    public void receiveEmail(String email) {
        Email email1 = new Email();
        email1.setEmail(email);
        Email savedEmail = emailRepository.save(email1);
        if (savedEmail == null) {
            throw new RuntimeException("Email could not be saved");
        }
        System.out.println("Email saved to the database: " + savedEmail.getEmail());

        sendWelcomeEmail(savedEmail.getEmail());
    }

    private void sendWelcomeEmail(String recipientEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Welcome to our Library");
        message.setText("Hello, welcome to our Library. We're glad to have you on board!");

        try {
            javaMailSender.send(message);
            System.out.println("Welcome email sent to: " + recipientEmail);
        } catch (Exception e) {
            System.out.println("Error sending welcome email: " + e.getMessage());
        }
    }
}
