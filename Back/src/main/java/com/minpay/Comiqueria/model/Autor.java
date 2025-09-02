package com.minpay.Comiqueria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa un Autor dentro del sistema de la Comiquería.
 * Un autor es una persona acreditada por la creación de uno o más productos (cómics, mangas, libros, etc.).
 * Esta entidad gestiona la información básica del autor y su relación con los productos.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "autores", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"aut_nombre", "aut_apellido"})
})
public class Autor {
    /**
     * Identificador único del autor en la base de datos.
     * Generado automáticamente mediante una estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aut_id")
    @ToString.Include
    private Long id;
    
    /**
     * Nombre del autor.
     * No puede ser nulo y tiene una longitud máxima de 30 caracteres.
     * Combinado con el apellido, forma una restricción de unicidad.
     */
    @NonNull
    @Size(max = 30)
    @Column(name = "aut_nombre", length = 30)
    @ToString.Include
    private String nombre;
    
    /**
     * Apellido del autor.
     * No puede ser nulo y tiene una longitud máxima de 30 caracteres.
     * Combinado con el nombre, forma una restricción de unicidad.
     */
    @NonNull
    @Size(max = 30)
    @Column(name = "aut_apellido", length = 30)
    @ToString.Include
    private String apellido;
    
    /**
     * Conjunto de productos asociados a este autor.
     * Representa una relación Many-to-Many donde un autor puede tener múltiples productos
     * y un producto puede tener múltiples autores.
     * La relación se mapea a través de la tabla de unión "productos-autores".
     */
    @ManyToMany
    @JoinTable(
        name = "productos-autores",
        joinColumns = @JoinColumn(name = "aut_id"),
        inverseJoinColumns = @JoinColumn(name = "prod_id")
    )
    private Set<Producto> productos = new LinkedHashSet<>();
    
    /**
     * Fecha en que el autor fue registrado en el sistema.
     * Se establece automáticamente en la fecha actual al momento de la creación.
     */
    @Column(name = "aut_fecha_alta")
    @ToString.Include
    private LocalDate fechaAlta = LocalDate.now();
    
    /**
     * Fecha en que el autor fue dado de baja del sistema (baja lógica).
     * Puede ser nulo si el autor aún está activo.
     */
    @Column(name = "aut_fecha_baja")
    @ToString.Include
    private LocalDate fechaBaja;
    
    /**
     * Indica si el autor está activo y vigente dentro del sistema.
     * Utilizado para la baja lógica. Por defecto es true.
     */
    @Column(name = "aut_esta_vigente")
    @ToString.Include
    private Boolean estaVigente = true;
}