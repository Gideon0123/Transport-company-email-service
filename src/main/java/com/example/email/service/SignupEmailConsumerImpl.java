package com.example.email.service;

import com.example.email.config.RabbitMQConfig;
import com.example.email.dto.CustomerSignupEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupEmailConsumerImpl implements SignupEmailConsumer{

    private final JavaMailSender mailSender;

    @RabbitListener(queues = RabbitMQConfig.SIGNUP_QUEUE)
    @Retryable(
            retryFor = { MailException.class, RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @Override
    public void consume(CustomerSignupEvent event) {

        System.out.println("EVENT RECEIVED");

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(event.getEmail());
        message.setSubject("Welcome to Gideon Transport Company!");
        message.setText("Hurray! \n" +
                "Dear " + event.getFirstName() + " " + event.getLastName() + "\n" +
                "Welcome to Gideon Transport Company \n" +
                "\n" +
                "We Hope to Serve you well as you Journey Around the Country and Beyond"
        );

        mailSender.send(message);

        System.out.println("EMAIL SENT");
    }

    @Recover
    public void recover(Exception e, CustomerSignupEvent event) {

        System.err.println(
                "FAILED TO SEND VERIFICATION EMAIL TO "
                        + event.getEmail()
        );
    }
}
