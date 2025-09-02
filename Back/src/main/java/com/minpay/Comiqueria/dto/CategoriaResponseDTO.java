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
public class CategoriaResponseDTO {
    private Long id;
    private String nombre;
    private Set<SubcategoriaResponseDTO> subcategorias = new LinkedHashSet<>();
    private Boolean estaVigente;
}