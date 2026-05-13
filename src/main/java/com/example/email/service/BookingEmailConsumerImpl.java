package com.example.email.service;

import com.example.email.dto.BookingCreatedEvent;
import com.example.email.config.RabbitMQConfig;
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
public class BookingEmailConsumerImpl implements BookingEmailConsumer {

    private final JavaMailSender mailSender;

    @RabbitListener(queues = RabbitMQConfig.BOOKING_QUEUE)
    @Retryable(
            retryFor = { MailException.class, RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void consume(BookingCreatedEvent event) {

        System.out.println("EVENT RECEIVED");

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(event.getEmail());
        message.setSubject("Booking Confirmed");

        message.setText(

                "Your Booking Order Has Been Confirmed with the The following \n" +
                        "\n" +
                        "Dear " + event.getCustomerName() + " \n" +
                        "Your Trip is from: " + event.getDepartureLocation() + " \n" +
                        "To: " + event.getDestinationLocation() + " \n" +
                        "The Departure time for this trip is: " + event.getDepartureTime() + " \n" +
                        "Number of Seats Booked: " + event.getSeats() + " \n" +
                        "The Price per Seat is: " + event.getPrice() + "\n" +
                        "Total Price: " + event.getTotalPrice()
        );

        mailSender.send(message);

        System.out.println("EMAIL SENT");
    }
    @Recover
    public void recover(Exception e, BookingCreatedEvent event) {

        System.err.println(
                "FAILED TO SEND VERIFICATION EMAIL TO "
                        + event.getEmail()
        );
    }
}