package com.minpay.Comiqueria.dto;

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
public class AutorRequestDTO {
    @NotBlank(message = "El nombre no puede estar vacío") // No nulo, no vacío
    @Size(max = 30, message = "El nombre no puede exceder los 30 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 30, message = "El apellido no puede exceder los 30 caracteres")
    private String apellido;
}
