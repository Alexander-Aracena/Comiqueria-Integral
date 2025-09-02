package com.minpay.Comiqueria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarruselRequestDTO {
    @NotBlank(message = "El subtítulo no puede estar vacío")
    @Size(max = 100, message = "El subtítulo no puede exceder los 100 caracteres")
    private String subtitulo;

    @NotBlank(message = "El texto no puede estar vacío")
    @Size(max = 255, message = "El texto no puede exceder los 255 caracteres")
    private String texto;

    @NotBlank(message = "La URL de la imagen no puede estar vacía")
    private String imagen;
    
    @NotBlank(message = "La URL del destino no puede estar vacío")
    private String urlDestino;
}