package com.minpay.Comiqueria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
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
 * Representa una categoría principal para organizar los productos (ej. "Cómics", "Manga", "Figuras").
 * Cada categoría se identifica por un nombre único y gestiona su propia vigencia
 * y las subcategorías asociadas.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "categorias")
public class Categoria {
    /**
     * Identificador único de la categoría en la base de datos.
     * Generado automáticamente mediante una estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;
    
    /**
     * Nombre de la categoría.
     * No puede ser nulo. Debe ser único y tiene una longitud máxima de 30 caracteres.
     */
    @NonNull
    @Size(max = 30)
    @Column(name = "cat_nombre", length = 30, unique = true)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String nombre;
    
    /**
     * Fecha en que la categoría fue registrada en el sistema.
     * Se establece automáticamente en la fecha y hora actual al momento de la creación.
     */
    @Column(name = "cat_fecha_alta")
    @ToString.Include
    private LocalDateTime fechaAlta = LocalDateTime.now();
    
    /**
     * Fecha en que la categoría fue dada de baja (baja lógica).
     * Puede ser nulo si la categoría aún está activa.
     */
    @Column(name = "cat_fecha_baja")
    @ToString.Include
    private LocalDateTime fechaBaja;
    
    /**
     * Indica si la categoría está activa y vigente en el sistema.
     * Utilizado para la baja lógica. Por defecto es 'true'.
     */
    @Column(name = "cat_esta_vigente")
    private Boolean estaVigente = true;
    
    /**
     * Conjunto de subcategorías asociadas a esta categoría.
     * Representa una relación One-to-Many, donde una categoría puede tener múltiples subcategorías.
     * El 'cascade = CascadeType.ALL' indica que las operaciones de persistencia se propagarán a
     * las subcategorías.
     */
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private Set<Subcategoria> subcategorias = new LinkedHashSet<>();
}