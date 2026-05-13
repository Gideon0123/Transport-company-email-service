package com.example.email.service;

import com.example.email.config.RabbitMQConfig;
import com.example.email.dto.VerificationEmailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerificationEmailConsumerImpl implements VerificationEmailConsumer {

    private final JavaMailSender mailSender;

    @RabbitListener(queues = RabbitMQConfig.VERIFICATION_QUEUE)
    @Retryable(
            retryFor = { MailException.class, RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @Override
    public void consume(VerificationEmailEvent event) {

        System.out.println("EVENT RECEIVED");

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(event.getTo());
        message.setSubject("Verification Code");
        message.setText(
                "Your verification code is: " + event.getCode()
        );

        mailSender.send(message);

        System.out.println("Verification email sent to " + event.getTo());
    }

    @Recover
    public void recover(Exception e, VerificationEmailEvent event) {

        System.err.println(
                "FAILED TO SEND VERIFICATION EMAIL TO "
                        + event.getTo()
        );
    }
}
