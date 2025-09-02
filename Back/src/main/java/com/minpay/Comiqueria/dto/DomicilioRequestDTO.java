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
public class DomicilioRequestDTO {
    @NotBlank(message = "La calle no puede estar vacía")
    @Size(max = 255, message = "La calle no puede exceder los 255 caracteres")
    private String calle;

    @NotBlank(message = "La altura no puede estar vacía")
    @Size(max = 10, message = "La altura no puede exceder los 10 caracteres")
    private String altura;

    @Size(max = 10, message = "El departamento no puede exceder los 10 caracteres")
    private String departamento;

    @NotBlank(message = "El código postal no puede estar vacío")
    @Size(max = 8, message = "El código postal no puede exceder los 8 caracteres")
    private String cp;

    @NotNull(message = "El ID de la localidad no puede ser nulo")
    private Long idLocalidad;

    @NotNull(message = "El ID del cliente no puede ser nulo")
    private Long idCliente;
}