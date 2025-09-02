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
public class ProvinciaRequestDTO {
    @NotBlank(message = "El nombre de la provincia no puede estar vacío")
    @Size(max = 30, message = "El nombre de la provincia no puede exceder los 30 caracteres")
    private String nombre;

    @NotNull(message = "El ID del país padre no puede ser nulo")
    private Long idPais;
}
