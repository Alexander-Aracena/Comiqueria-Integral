package com.minpay.Comiqueria.dto;

import com.minpay.Comiqueria.model.Rol;
import java.time.LocalDateTime;
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
public class UsuarioResponseDTO {
    private Long id;
    private String email;
    private Rol rol;
    private Boolean estaActivo;
    private LocalDateTime fechaAlta;
    private Long idClienteAsociado;
}
