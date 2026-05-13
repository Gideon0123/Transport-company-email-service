package com.example.email.service;

import com.example.email.dto.BookingCreatedEvent;

public interface BookingEmailConsumer {
    void consume(BookingCreatedEvent event);
}
