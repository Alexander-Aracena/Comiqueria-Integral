package com.minpay.Comiqueria.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDTO {
    @NotBlank(message = "El título del producto no puede estar vacío")
    private String titulo;

    @NotNull(message = "El precio del producto no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio debe ser un valor positivo o cero")
    private BigDecimal precio;

    @NotBlank(message = "La descripción del producto no puede estar vacía")
    private String descripcion;

    @NotBlank(message = "La URL de la tapa del producto no puede estar vacía")
    private String tapa; // Aquí iría la URL o el path de la imagen

    @NotBlank(message = "El ISBN no puede estar vacío")
    @Size(max = 30, message = "El ISBN no puede exceder los 30 caracteres")
    private String isbn;

    @Min(value = 0, message = "El peso debe ser un valor positivo o cero")
    private int peso;

    @NotBlank(message = "Las dimensiones no pueden estar vacías")
    @Size(max = 30, message = "Las dimensiones no pueden exceder los 30 caracteres")
    private String dimensiones;

    @Min(value = 0, message = "Las páginas deben ser un valor positivo o cero")
    private int paginas;

    private Set<Long> idAutores; // Lista de IDs de autores
    private Long idSubcategoria;
    private Long idEditorial;

    // Las banderas de visibilidad pueden ser booleanos en el request
    private Boolean esNovedad = false; // Valor por defecto
    private Boolean esOferta = false;
    private Boolean esMasVendido = false;
    private Boolean esVisibleEnHome = false;
}
