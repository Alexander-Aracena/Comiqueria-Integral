package com.minpay.Comiqueria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
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
@Table(name = "domicilios")
public class Domicilio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dom_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;
    
    @NonNull
    @Column(columnDefinition = "TEXT", name = "dom_calle")
    @EqualsAndHashCode.Include
    @ToString.Include
    private String calle;
    
    @NonNull
    @Size(max = 10)
    @Column(name = "dom_altura", length = 10)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String altura;
    
    @Size(max = 10)
    @Column(name = "dom_depto", length = 10)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String departamento;
    
    @NonNull
    @Size(max = 8)
    @Column(name = "dom_cp", length = 8)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String cp;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    @JoinColumn(name = "dom_loc_id", nullable = false)
    private Localidad localidad;
    
    @ManyToOne
    @JoinColumn(name = "dom_cte_id", nullable = false)
    private Cliente cliente;
    
    @Column(name = "dom_fecha_alta")
    @ToString.Include
    private LocalDate fechaAlta = LocalDate.now();
    
    @Column(name = "dom_fecha_baja")
    @ToString.Include
    private LocalDate fechaBaja;
    
    @Column(name = "dom_esta_vigente")
    @ToString.Include
    private Boolean estaVigente = true;
}