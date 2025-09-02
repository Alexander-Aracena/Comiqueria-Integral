package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.ForgotPasswordRequestDTO;
import com.minpay.Comiqueria.dto.LoginRequestDTO;
import com.minpay.Comiqueria.dto.LoginResponseDTO;
import com.minpay.Comiqueria.dto.RegisterRequestDTO;
import com.minpay.Comiqueria.dto.ResetPasswordRequestDTO;
import com.minpay.Comiqueria.dto.UsuarioResponseDTO;
import com.minpay.Comiqueria.exceptions.ChangePasswordRequiredException;
import com.minpay.Comiqueria.exceptions.ResourceNotFoundException;
import com.minpay.Comiqueria.service.EmailService;
import com.minpay.Comiqueria.service.interfaces.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final IAuthService authService;

    @Autowired
    private EmailService emailService;

    // Inyección de dependencia del servicio de autenticación
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(
            @Valid @RequestBody RegisterRequestDTO request
    ) {
        // Llama al servicio de autenticación para realizar el registro
        UsuarioResponseDTO response = this.authService.register(request);

        // Retorna la respuesta con el estado HTTP 201 Created
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loguearUsuario(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        try {
            // Llama al servicio de autenticación para realizar el login
            LoginResponseDTO response = this.authService.login(request);

            // Retorna la respuesta con el estado HTTP 200 OK
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ChangePasswordRequiredException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"message\": \"" + ex.getMessage() + "\", \"action\": \"CHANGE_PASSWORD\"}");
        } catch (Exception ex) {
            // Para otras excepciones de autenticación (ej. credenciales inválidas)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\": \"Credenciales inválidas\"}");
        }
    }

    @PostMapping("/change-password-forced")
    public ResponseEntity<?> forceChangePassword(
            @RequestParam String username,
            @RequestParam String newPassword
    ) {
        try {
            LoginResponseDTO authResponse = this.authService.forceChangePassword(username, newPassword);
            return ResponseEntity.ok(authResponse); // Contraseña cambiada y login exitoso
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"Usuario no encontrado.\"}");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error al cambiar la contraseña.\"}");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        String resetToken = authService.forgotPassword(request.getEmail());
        emailService.sendPasswordResetEmail(request.getEmail(), resetToken);

        return ResponseEntity.ok("Si el email está registrado, recibirás un enlace para restablecer tu contraseña.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("La contraseña ha sido restablecida con éxito.");
    }
}
