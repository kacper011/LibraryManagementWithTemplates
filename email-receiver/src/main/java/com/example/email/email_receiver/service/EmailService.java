package com.example.email.email_receiver.service;

import com.example.email.email_receiver.model.Email;
import com.example.email.email_receiver.repository.EmailRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final EmailRepository emailRepository;
    @Autowired
    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
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
    }
}
