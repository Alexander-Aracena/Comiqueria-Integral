package com.minpay.Comiqueria.dto;

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
public class SubcategoriaResponseDTO {
    private Long id;
    private String nombre;
    private Boolean estaVigente;
    private CategoriaBasicaDTO categoria;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoriaBasicaDTO {
        private Long id;
        private String nombre;
    }
}
