package com.minpay.Comiqueria.config;

import com.minpay.Comiqueria.model.Rol;
import com.minpay.Comiqueria.model.Usuario;
import com.minpay.Comiqueria.repository.IUsuarioRepository;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final String PASS_ADMIN = "admin176";

    public AdminInitializer(IUsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existe un usuario con el rol ADMIN
        if (usuarioRepository.findByRol(Rol.ADMIN).isEmpty()) {
            // Si no hay ningún administrador, crear uno
            Usuario admin = new Usuario();
            admin.setEmail("admin@lacomiqueria.com");
            admin.setPasswordHash(passwordEncoder.encode(PASS_ADMIN)); // Contraseña 'admin' hasheada
            admin.setRol(Rol.ADMIN);
            admin.setEstaActivo(Boolean.TRUE);
            admin.setFechaAlta(LocalDateTime.now());
            
            // Campo para forzar el cambio de contraseña:
            admin.setResetPasswordToken("INITIAL_SETUP"); // Usamos este campo como marcador

            // Una fecha lejana para que el token no expire, solo es un marcador
            admin.setResetTokenExpirationDate(
                LocalDateTime.now().plusYears(100)
            );
            usuarioRepository.save(admin);
            System.out.println(
                "Usuario 'admin' creado exitosamente con contraseña '" + PASS_ADMIN + "'."
            );
        } else {
            System.out.println("Ya existe al menos un usuario administrador. No se creó uno nuevo.");
        }
    }
}
