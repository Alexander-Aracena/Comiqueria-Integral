package com.minpay.Comiqueria.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @Email(message = "El formato del email no es válido")
    @NotBlank(message = "La dirección de correo electrónico no puede estar vacía")
    @Size(max = 100, message = "La dirección de correo electrónico debe tener {max} caracteres")
    private String email;
    
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 30, message = "La contraseña debe tener entre 8 y 30 caracteres")
    private String contrasenia;
}
