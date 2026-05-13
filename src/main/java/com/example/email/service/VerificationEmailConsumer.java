package com.example.email.service;

import com.example.email.dto.VerificationEmailEvent;

public interface VerificationEmailConsumer {
    void consume(VerificationEmailEvent event);
}
