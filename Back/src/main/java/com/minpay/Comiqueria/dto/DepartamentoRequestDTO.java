package com.minpay.Comiqueria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartamentoRequestDTO {
    @NotBlank(message = "El nombre del departamento no puede estar vacío")
    @Size(max = 50, message = "El nombre del departamento no puede exceder los 50 caracteres")
    private String nombre;

    @NotNull(message = "El ID de la provincia padre no puede ser nulo")
    private Long idProvincia;
}
