package com.minpay.Comiqueria.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AutorResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private Boolean estaVigente;
}
