package com.minpay.Comiqueria.dto;

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
public class ProvinciaResponseDTO {
    private Long id;
    private String nombre;
    private Boolean estaVigente;
    private PaisBasicoDTO pais;
    private Set<DepartamentoResponseDTO> departamentos = new LinkedHashSet<>();
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaisBasicoDTO {
        private Long id;
        private String nombre;
    }
}
