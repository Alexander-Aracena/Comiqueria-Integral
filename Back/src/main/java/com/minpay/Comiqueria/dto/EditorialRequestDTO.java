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
public class EditorialRequestDTO {
    @NotBlank(message = "El nombre de la editorial no puede estar vacío")
    @Size(max = 50, message = "El nombre de la editorial no puede exceder los 50 caracteres")
    private String nombre;
}
