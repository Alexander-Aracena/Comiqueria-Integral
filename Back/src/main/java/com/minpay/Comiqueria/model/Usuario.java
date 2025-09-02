package com.minpay.Comiqueria.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
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
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usr_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NonNull
    @Email
    @Column(name = "usr_email", unique = true, length = 100)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String email;

    @NonNull
    @Size(max = 255) // Para almacenar el hash de la contraseña
    @Column(name = "usr_password_hash", length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(name = "usr_rol", length = 20)
    @ToString.Include
    private Rol rol;

    @Column(name = "usr_fecha_alta")
    @ToString.Include
    private LocalDateTime fechaAlta = LocalDateTime.now();
    
    @Column(name = "usr_fecha_baja")
    @ToString.Include
    private LocalDateTime fechaBaja;

    @Column(name = "usr_esta_activo")
    private Boolean estaActivo = true;
    
    @Column(name = "usr_ultimo_login")
    @ToString.Include
    private LocalDateTime ultimoLogin;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cliente cliente;
    
    @Column(name = "usr_rptoken")
    private String resetPasswordToken;
    
    @Column(name = "usr_rtokened")
    private LocalDateTime resetTokenExpirationDate;
}