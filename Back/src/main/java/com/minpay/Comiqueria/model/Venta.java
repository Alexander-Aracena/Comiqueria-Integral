package com.minpay.Comiqueria.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vta_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NonNull
    @Column(name = "vta_fecha_vta", nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private LocalDateTime fechaVenta = LocalDateTime.now();

    @NonNull
    @Column(name = "vta_total", nullable = false, precision = 10, scale = 2)
    @EqualsAndHashCode.Include
    @ToString.Include
    private BigDecimal total;

    @NonNull
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private Set<LineaVenta> lineas = new LinkedHashSet<>();

    @NonNull
    @ManyToOne
    @JoinColumn(name = "vta_cte_id", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(name = "vta_estado", length = 30, nullable = false)
    @ToString.Include
    private EstadoVenta estado;
}