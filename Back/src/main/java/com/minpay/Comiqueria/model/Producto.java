package com.minpay.Comiqueria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;
    
    @NonNull
    @Column(columnDefinition = "TEXT", name = "prod_titulo")
    @EqualsAndHashCode.Include
    @ToString.Include
    private String titulo;
    
    @NonNull
    @Column(name = "prod_precio")
    @EqualsAndHashCode.Include
    @ToString.Include
    private BigDecimal precio;
    
    @NonNull
    @Column(columnDefinition = "TEXT", name = "prod_descripcion")
    @EqualsAndHashCode.Include
    @ToString.Include
    private String descripcion;
    
    @NonNull
    @Column(columnDefinition = "TEXT", name = "prod_tapa")
    @EqualsAndHashCode.Include
    @ToString.Include
    private String tapa;
    
    @NonNull
    @Size(max = 30)
    @Column(name = "prod_isbn", length = 30, unique = true)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String isbn;
    
    @Column(name = "prod_peso")
    @EqualsAndHashCode.Include
    @ToString.Include
    private int peso;
    
    @NonNull
    @Size(max = 30)
    @Column(name = "prod_dimensiones", length = 30)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String dimensiones;
    
    @Column(name = "prod_paginas")
    @EqualsAndHashCode.Include
    @ToString.Include
    private int paginas;
    
    @ManyToMany(mappedBy = "productos")
    private Set<Autor> autores = new LinkedHashSet<>();
    
    @ManyToOne
    @JoinColumn(name = "prod_subcat_id")
    private Subcategoria subcategoria;
    
    @ManyToOne
    @JoinColumn(name = "prod_edit_id")
    private Editorial editorial;
    
    @Column(name = "prod_esNovedad")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Boolean esNovedad;
    
    @Column(name = "prod_esOferta")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Boolean esOferta;
    
    @Column(name = "prod_esMasVendido")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Boolean esMasVendido;
    
    @Column(name = "prod_index")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Boolean esVisibleEnHome;
    
    @Column(name = "prod_fecha_alta")
    @ToString.Include
    private LocalDateTime fechaAlta = LocalDateTime.now();
    
    @Column(name = "prod_fecha_baja")
    @ToString.Include
    private LocalDateTime fechaBaja;
    
    @Column(name = "prod_esta_vigente")
    @ToString.Include
    private Boolean estaVigente = true;
    
    @ManyToMany(mappedBy = "favoritos")
    private Set<Cliente> clientes = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "producto")
    private Set<LineaVenta> lineasVenta = new LinkedHashSet<>();
}