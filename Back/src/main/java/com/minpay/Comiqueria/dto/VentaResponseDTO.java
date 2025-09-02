package com.minpay.Comiqueria.dto;

import com.minpay.Comiqueria.model.EstadoVenta;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class VentaResponseDTO {
    private Long id;
    private LocalDateTime fechaVenta;
    private BigDecimal total;
    private EstadoVenta estado; // El enum EstadoVenta se serializará como String por defecto

    // Información esencial del cliente asociado a la venta
    private ClienteBasicoDTO cliente;

    // Las líneas de venta asociadas a esta venta
    private Set<LineaVentaResponseDTO> lineas = new LinkedHashSet<>();

    // --- DTOs Anidados para relaciones ---

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClienteBasicoDTO {
        private Long id;
        private String nombre;
        private String apellido;
        private String tipoDoc;
        private String nroDocumento;
    }
}
