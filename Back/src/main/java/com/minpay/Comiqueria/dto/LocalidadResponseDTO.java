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
public class LocalidadResponseDTO {
    private Long id;
    private String nombre;
    private Boolean estaVigente;
    private DepartamentoBasicoDTO departamento;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartamentoBasicoDTO {
        private Long id;
        private String nombre;
        private ProvinciaBasicaDTO provincia;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProvinciaBasicaDTO {
        private Long id;
        private String nombre;
        private PaisBasicoDTO pais;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaisBasicoDTO {
        private Long id;
        private String nombre;
    }
}
