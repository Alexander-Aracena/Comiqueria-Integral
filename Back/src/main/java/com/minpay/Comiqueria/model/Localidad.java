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

@Getter
@Setter
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "localidades", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"loc_nombre", "dep_id"})  // Nombre de loc. + ID de Departamento debe ser único
})
public class Localidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loc_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NonNull
    @Size(max = 30)
    @Column(name = "loc_nombre", length = 30)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String nombre;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY) // Una localidad pertenece a un departamento
    @JoinColumn(name = "loc_dep_id", nullable = false) // <-- FK a Departamento
    private Departamento departamento;

    @OneToMany(mappedBy = "localidad") // Un domicilio pertenece a una localidad
    private Set<Domicilio> domicilios = new LinkedHashSet<>();

    @Column(name = "loc_fecha_alta")
    private LocalDateTime fechaAlta = LocalDateTime.now();

    @Column(name = "loc_fecha_baja")
    private LocalDateTime fechaBaja;

    @Column(name = "loc_esta_vigente")
    private Boolean estaVigente = true;
}
