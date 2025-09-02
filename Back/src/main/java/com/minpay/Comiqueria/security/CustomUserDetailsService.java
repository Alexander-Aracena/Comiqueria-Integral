package com.minpay.Comiqueria.security;

import com.minpay.Comiqueria.model.Usuario;
import com.minpay.Comiqueria.repository.IUsuarioRepository;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private IUsuarioRepository usuarioRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca el usuario por nombre de usuario en la base de datos
        Usuario usuario = this.usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(
                "Usuario no encontrado con nombre de usuario: " + email
            ));
        
        // Construye y retorna un objeto UserDetails de Spring Security.
        // Se asume que la contraseña del usuario ya está codificada en la base de datos.
        // Se mapea el rol del usuario a una autoridad de Spring Security con el prefijo "ROLE_".
        return new User(
            usuario.getEmail(),
            usuario.getPasswordHash(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }
}
