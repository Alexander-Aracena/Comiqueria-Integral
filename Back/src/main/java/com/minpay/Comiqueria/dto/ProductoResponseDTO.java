package com.minpay.Comiqueria.dto;

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
public class ProductoResponseDTO {
    private Long id;
    private String titulo;
    private BigDecimal precio;
    private String descripcion;
    private String tapa;
    private String isbn;
    private int peso;
    private String dimensiones;
    private int paginas;
    private Set<AutorBasicoDTO> autores = new LinkedHashSet<>();
    private SubcategoriaBasicoDTO subcategoria;
    private EditorialBasicoDTO editorial;
    private Boolean esNovedad;
    private Boolean esOferta;
    private Boolean esMasVendido;
    private Boolean esVisibleEnHome;
    private Boolean estaVigente;
    private LocalDateTime fechaAlta;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AutorBasicoDTO {
        private Long id;
        private String nombre;
        private String apellido;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubcategoriaBasicoDTO {
        private Long id;
        private String nombre;
        private CategoriaBasicoDTO categoria;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoriaBasicoDTO {
        private Long id;
        private String nombre;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditorialBasicoDTO {
        private Long id;
        private String nombre;
    }
}
