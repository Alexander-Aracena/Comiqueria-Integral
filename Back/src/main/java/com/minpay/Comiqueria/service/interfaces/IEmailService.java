package com.minpay.Comiqueria.service.interfaces;

public interface IEmailService {
    void sendPasswordResetEmail(String to, String resetToken);
}
