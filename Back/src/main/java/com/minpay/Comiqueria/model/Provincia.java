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
@Table(name = "provincias")
public class Provincia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prov_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;
    
    @NonNull
    @Size(max = 30)
    @Column(name = "prov_nombre", length = 30, unique = true)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String nombre;
    
    @OneToMany(mappedBy = "provincia", cascade = CascadeType.ALL)
    private Set<Departamento> departamentos = new LinkedHashSet<>();
    
    @NonNull
    @ManyToOne
    @JoinColumn(name = "prov_pais_id")
    private Pais pais;
    
    @Column(name = "prov_fecha_alta")
    @ToString.Include
    private LocalDateTime fechaAlta = LocalDateTime.now();
    
    @Column(name = "prov_fecha_baja")
    @ToString.Include
    private LocalDateTime fechaBaja;
    
    @Column(name = "prov_esta_vigente")
    private Boolean estaVigente = true;
}