package com.minpay.Comiqueria.dto;

import com.minpay.Comiqueria.model.Sexo;
import com.minpay.Comiqueria.model.TipoDoc;
import java.time.LocalDate;
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
public class ClienteResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNac; // Nomenclatura camelCase para DTOs
    private Sexo sexo;        // El enum Sexo se serializará como String
    private String nroDocumento;
    private TipoDoc tipoDoc;     // El enum TipoDoc se serializará como String
    private String telefono;
    private Boolean estaVigente; // El estado general de vigencia del cliente
    private LocalDateTime fechaAlta;

    // Relaciones (usando DTOs anidados o IDs para evitar recursión y mostrar solo lo esencial)
    private UsuarioBasicoDTO usuario; // Información básica del usuario asociado
    private Set<ProductoBasicoDTO> favoritos = new LinkedHashSet<>(); // Solo IDs/títulos si son muchos
    private Set<VentaBasicoDTO> ventasRecientes = new LinkedHashSet<>(); // Solo ID y fecha de ventas recientes

    // --- DTOs Anidados para relaciones ---

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioBasicoDTO {
        private Long id;
        private String email;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductoBasicoDTO {
        private Long id;
        private String titulo;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VentaBasicoDTO {
        private Long id;
        private LocalDateTime fechaVenta;
    }
}
