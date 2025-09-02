package com.minpay.Comiqueria.dto;

import java.math.BigDecimal;
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
public class LineaVentaResponseDTO {
    private Long id;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    // Información esencial del producto asociado a esta línea de venta
    private ProductoBasicoDTO producto;

    // --- DTO Anidado para el Producto ---
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductoBasicoDTO {
        private Long id;
        private String titulo;
    }
}
