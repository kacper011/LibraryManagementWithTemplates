package com.example.email.email_receiver.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQReceiverConfig {

    @Bean
    public Queue emailQueue() {
        return new Queue("emailQueue", false);
    }
}
