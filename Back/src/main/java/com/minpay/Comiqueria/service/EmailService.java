package com.minpay.Comiqueria.service;

import com.minpay.Comiqueria.service.interfaces.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${app.frontend.reset-password-url}")
    private String frontendResetPasswordUrl;
    
    @Override
    @Async
    public void sendPasswordResetEmail(String to, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("alexander.aracena.cc@gmail.com");
        message.setTo(to);
        message.setSubject("Restablecimiento de contraseña para Comiquería");

        String resetLink = frontendResetPasswordUrl + "?token=" + resetToken;

        message.setText(
            """
            Hola.
            Has solicitado restablecer tu contraseña. Por favor, haz clic en el siguiente enlace para continuar:
            """
            + resetLink +
            """
            Este enlace expirará en 1 hora. Si no solicitaste un restablecimiento de contraseña, por favor ignora este correo.
            Saludos.
            El equipo de Comiquería
            """);
        
        mailSender.send(message);
    }
}
