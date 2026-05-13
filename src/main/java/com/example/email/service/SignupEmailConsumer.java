package com.example.email.service;

import com.example.email.dto.CustomerSignupEvent;

public interface SignupEmailConsumer {
    void consume(CustomerSignupEvent event);
}
