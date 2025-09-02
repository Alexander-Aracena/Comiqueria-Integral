package com.minpay.Comiqueria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa un elemento de carrusel (slide) que se muestra en la interfaz de usuario,
 * típicamente en la página de inicio o en secciones destacadas.
 * Cada elemento de carrusel incluye un subtítulo, texto descriptivo, una imagen,
 * un enlace de destino, un orden de visualización y un estado de actividad.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "carruseles")
public class Carrusel {
    /**
     * Identificador único del elemento del carrusel en la base de datos.
     * Generado automáticamente mediante una estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long id;
    
    /**
     * Subtítulo breve o encabezado del elemento del carrusel.
     * No puede ser nulo y tiene una longitud máxima de 50 caracteres.
     */
    @NonNull
    @Size(max = 50)
    @Column(name = "car_subtitulo", length = 50)
    @ToString.Include
    private String subtitulo;
    
    /**
     * Texto descriptivo principal del elemento del carrusel.
     * No puede ser nulo y tiene una longitud máxima de 100 caracteres.
     */
    @NonNull
    @Size(max = 100)
    @Column(name = "car_texto", length = 100)
    @ToString.Include
    private String texto;
    
    /**
     * URL o ruta de la imagen que se mostrará en el carrusel.
     * No puede ser nulo. Almacenado como texto (generalmente una URL).
     */
    @NonNull
    @Column(name = "car_imagen", columnDefinition = "TEXT")
    @ToString.Include
    private String imagen;
    
    /**
     * URL a la que se redirigirá al usuario al hacer clic en el elemento del carrusel.
     * No puede ser nulo. Almacenado como texto.
     */
    @NonNull
    @Column(name = "car_destino", columnDefinition = "TEXT")
    @ToString.Include
    private String urlDestino;
    
    /**
     * Orden de visualización del elemento dentro del carrusel.
     * Los elementos con un valor de orden menor se mostrarán primero.
     * Puede ser nulo si no se requiere un orden específico.
     */
    @Column(name = "car_orden")
    private Integer orden;
    
    /**
     * Indica si el elemento del carrusel está activo y debe mostrarse.
     * Por defecto es true. Utilizado para habilitar/deshabilitar elementos sin eliminarlos.
     */
    @Column(name = "car_esta_activo")
    private Boolean estaActivo = true;
}