package com.minpay.Comiqueria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
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
 * Representa un cliente registrado en la Comiquería.
 * Almacena la información personal, datos de contacto, dirección,
 * su cuenta de usuario asociada, productos favoritos y el historial de ventas.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "clientes")
public class Cliente {
    /**
     * Identificador único del cliente en la base de datos.
     * Generado automáticamente mediante una estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cte_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    /**
     * Nombre del cliente.
     * No puede ser nulo y tiene una longitud máxima de 30 caracteres.
     */
    @NonNull
    @Size(max = 30)
    @Column(name = "cte_nombre", length = 30)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String nombre;

    /**
     * Apellido del cliente.
     * No puede ser nulo y tiene una longitud máxima de 30 caracteres.
     */
    @NonNull
    @Size(max = 30)
    @Column(name = "cte_apellido", length = 30)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String apellido;

    /**
     * Fecha de nacimiento del cliente.
     * No puede ser nulo.
     */
    @NonNull
    @Column(name = "cte_fecha_nac")
    @EqualsAndHashCode.Include
    @ToString.Include
    private LocalDate fechaNac;

    /**
     * Sexo del cliente, representado por un valor de enumeración.
     * No puede ser nulo.
     */
    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(name = "cte_sexo")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Sexo sexo;

    /**
     * Número de documento de identidad del cliente.
     * No puede ser nulo y debe ser único en la base de datos.
     */
    @NonNull
    @Column(name = "cte_documento", unique = true)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String nroDocumento;

    /**
     * Tipo de documento de identidad del cliente, representado por un valor de enumeración.
     * No puede ser nulo.
     */
    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(name = "cte_tipo_doc")
    @EqualsAndHashCode.Include
    @ToString.Include
    private TipoDoc tipoDoc;

    /**
     * Conjunto de domicilios asociados al cliente.
     * Representa una relación One-to-Many, donde un cliente puede tener múltiples domicilios.
     * Las operaciones de persistencia en Cliente se propagarán a Domicilio.
     */
    @NonNull
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.PERSIST)
    private Set<Domicilio> domicilios = new LinkedHashSet<>();

    /**
     * Número de teléfono del cliente.
     * No puede ser nulo y tiene una longitud máxima de 20 caracteres.
     */
    @NonNull
    @Size(max = 20)
    @Column(name = "cte_telefono", length = 20)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String telefono;
    
    /**
     * Usuario asociado a este perfil de cliente.
     * Representa una relación One-to-One donde un cliente tiene un único usuario
     * para la autenticación y un usuario solo puede estar asociado a un cliente.
     * La columna 'usr_id' es la clave foránea y debe ser única y no nula.
     */
    @NonNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usr_id", unique = true, nullable = false)
    private Usuario usuario;

    /**
     * Fecha y hora en que el cliente fue registrado en el sistema.
     * Se establece automáticamente en la fecha y hora actual al momento de la creación.
     */
    @Column(name = "cte_fecha_alta")
    private LocalDateTime fechaAlta = LocalDateTime.now();

    /**
     * Fecha y hora en que el cliente fue dado de baja (baja lógica).
     * Puede ser nulo si el cliente aún está activo.
     */
    @Column(name = "cte_fecha_baja")
    private LocalDateTime fechaBaja;

    /**
     * Indica si el cliente está activo y vigente en el sistema.
     * Utilizado para la baja lógica. Por defecto es 'true'.
     */
    @Column(name = "cte_esta_vigente")
    private Boolean estaVigente = Boolean.TRUE;

    /**
     * Conjunto de productos marcados como favoritos por el cliente.
     * Representa una relación Many-to-Many con la entidad Producto,
     * utilizando una tabla de unión 'cte_producto_favorito'.
     */
    @ManyToMany
    @JoinTable(
            name = "cte_producto_favorito",
            joinColumns = @JoinColumn(name = "cte_id"),
            inverseJoinColumns = @JoinColumn(name = "prod_id")
    )
    private Set<Producto> favoritos = new LinkedHashSet<>();

    /**
     * Conjunto de ventas realizadas por el cliente.
     * Representa una relación One-to-Many, donde un cliente puede tener múltiples ventas.
     */
    @OneToMany(mappedBy = "cliente")
    private Set<Venta> ventas = new LinkedHashSet<>();
}