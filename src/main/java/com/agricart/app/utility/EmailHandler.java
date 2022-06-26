package com.agricart.app.utility;

import io.swagger.v3.oas.annotations.servers.Server;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailHandler {

    private final JavaMailSender mailSender;
    private final Environment env;

    public void sendSimpleEmail(String to , String body, String subject){

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(env.getProperty("spring.mail.username"));
        message.setTo(to);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

    }

}
