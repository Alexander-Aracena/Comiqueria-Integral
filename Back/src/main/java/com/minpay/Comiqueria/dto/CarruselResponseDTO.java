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
public class CarruselResponseDTO {
    private Long id;
    private String subtitulo;
    private String texto;
    private String imagen;
    private String urlDestino;
    private Integer orden;
    private Boolean estaActivo;
}
