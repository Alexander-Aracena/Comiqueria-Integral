package com.minpay.Comiqueria.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequestDTO {
    @NotNull(message = "El ID del cliente no puede ser nulo")
    private Long idCliente;

    @NotEmpty(message = "La venta debe tener al menos una línea de venta")
    private Set<LineaVentaRequestDTO> lineas;
}