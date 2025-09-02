package com.minpay.Comiqueria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaisRequestDTO {
    @NotBlank(message = "El nombre del país no puede estar vacío")
    @Size(max = 30, message = "El nombre del país no puede exceder los 30 caracteres")
    private String nombre;
}
