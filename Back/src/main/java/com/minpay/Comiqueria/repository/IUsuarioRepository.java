package com.minpay.Comiqueria.repository;

import com.minpay.Comiqueria.model.Rol;
import com.minpay.Comiqueria.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>,
    JpaSpecificationExecutor<Usuario> {
    /**
     * Verifica la existencia de un usuario por su dirección de correo electrónico.
     *
     * @param email La dirección de correo electrónico a buscar.
     * @return {@code true} si existe un usuario con la dirección de correo electrónico
     * dada, {@code false} en caso contrario.
     */
    boolean existsByEmail(String email);
    
    Optional<Usuario> findByEmail(String email);
    
    Optional<Usuario> findByRol(Rol rol);
    
    Optional<Usuario> findByResetPasswordToken(String resetPasswordToken);
}
