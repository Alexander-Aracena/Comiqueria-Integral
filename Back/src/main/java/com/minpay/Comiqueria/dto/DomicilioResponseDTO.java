package com.minpay.Comiqueria.dto;

import com.minpay.Comiqueria.model.TipoDoc;
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
public class DomicilioResponseDTO {
    private Long id;
    private String calle;
    private String altura;
    private String departamento;
    private String cp;
    private Boolean estaVigente;
    private LocalidadBasicoDTO localidad;
    private ClienteBasicoDTO cliente;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocalidadBasicoDTO {
        private Long id;
        private String nombre;
        private DepartamentoBasicoDTO departamento;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClienteBasicoDTO {
        private Long id;
        private String nombre;
        private String apellido;
        private TipoDoc tipoDoc;
        private String nroDocumento;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartamentoBasicoDTO {
        private Long id;
        private String nombre;
        private ProvinciaBasicoDTO provincia;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProvinciaBasicoDTO {
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
