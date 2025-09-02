package com.minpay.Comiqueria.config;

import com.minpay.Comiqueria.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {
    // Inyectamos nuestro CustomUserDetailsService y el PasswordEncoder
    // Spring ya sabe cómo crearlos porque CustomUserDetailsService tiene @Service
    // y PasswordEncoder es un @Bean.
    private final CustomUserDetailsService customUserDetailsService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Le decimos que use nuestro servicio
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder()); // Le decimos que use nuestro codificador
        return authProvider;
    }
}
