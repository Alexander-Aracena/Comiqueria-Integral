package com.minpay.Comiqueria.service;

import com.minpay.Comiqueria.dto.UsuarioRequestDTO;
import com.minpay.Comiqueria.dto.UsuarioResponseDTO;
import com.minpay.Comiqueria.exceptions.InvalidOperationException;
import com.minpay.Comiqueria.exceptions.ResourceAlreadyExistsException;
import com.minpay.Comiqueria.mapper.IUsuarioMapper;
import com.minpay.Comiqueria.model.Rol;
import com.minpay.Comiqueria.model.Usuario;
import com.minpay.Comiqueria.repository.IUsuarioRepository;
import com.minpay.Comiqueria.repository.specification.UsuarioSpecifications;
import com.minpay.Comiqueria.service.interfaces.IUsuarioService;
import com.minpay.Comiqueria.utils.Utils;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UsuarioService implements IUsuarioService {
    @Autowired
    private IUsuarioRepository usuarioRepository;
    
    @Autowired
    private IUsuarioMapper usuarioMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO getUsuario(Long id) {
        Usuario usuario = Utils.findByIdOrThrow(usuarioRepository, id, Usuario.class);
        return this.usuarioMapper.toUsuarioResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> getUsuarios(List<Long> ids, String email, Rol rol, Long idCliente, Boolean estaActivo) {
        Specification<Usuario> specs = UsuarioSpecifications.byCriterios(
            ids, email, rol, idCliente, estaActivo
        );
        List<Usuario> usuarios = this.usuarioRepository.findAll(specs);
        return Utils.mapearListaA(usuarios, this.usuarioMapper::toUsuarioResponseDTO);
    }

    @Override
    public UsuarioResponseDTO createUsuario(UsuarioRequestDTO usuarioDTO) {
        String email = usuarioDTO.getEmail();
        if (this.usuarioRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Ya existe un usuario creado con ese email en uso");
        }
        Rol rolAAsignar = Rol.CLIENTE;
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        boolean isAdminCaller = authentication != null && authentication.getAuthorities().stream()
            .anyMatch(user -> user.getAuthority().equals("ROLE_ADMIN"));
        
        if (isAdminCaller && usuarioDTO.getRol() != null) {
            rolAAsignar = usuarioDTO.getRol();
        }
        
        Usuario usuario = this.usuarioMapper.toUsuario(usuarioDTO);
        usuario.setRol(rolAAsignar);
        usuario.setPasswordHash(this.passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario = this.usuarioRepository.save(usuario);
        return this.usuarioMapper.toUsuarioResponseDTO(usuario);
    }

    @Override
    public UsuarioResponseDTO editUsuario(Long id, UsuarioRequestDTO usuarioDTO) {
        String emailElegido = usuarioDTO.getEmail();
        Rol rolElegido = usuarioDTO.getRol();
        String nuevaContrasenia = usuarioDTO.getPassword();

        Usuario usuario = Utils.findByIdOrThrow(usuarioRepository, id, Usuario.class);
        usuarioMapper.updateUsuarioFromDTO(usuarioDTO, usuario);

        if (emailElegido != null && !emailElegido.isBlank()) {
            if (!emailElegido.equals(usuario.getEmail())) {
                if (usuarioRepository.existsByEmail(emailElegido)) {
                    throw new ResourceAlreadyExistsException(
                        "El email '" + emailElegido + "' ya está registrado."
                    );
                }
                usuario.setEmail(emailElegido);
            }
        }

        if (rolElegido != null) {
            usuario.setRol(rolElegido);
        }

        // Hashea la nueva contraseña antes de guardarla, si se proporcionó una y no está en blanco
        if (nuevaContrasenia != null && !nuevaContrasenia.isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(nuevaContrasenia));
        }
        usuario = usuarioRepository.save(usuario);

        return usuarioMapper.toUsuarioResponseDTO(usuario);
    }

    @Override
    public void deleteUsuario(Long id) {
        Usuario usuario = Utils.findByIdOrThrow(usuarioRepository, id, Usuario.class);
        if (usuario.getEstaActivo().equals(Boolean.FALSE)) {
            throw new InvalidOperationException("El usuario ya fue dado de baja");
        }
        usuario.setFechaBaja(LocalDateTime.now());
        usuario.setEstaActivo(Boolean.FALSE);
        this.usuarioRepository.save(usuario);
    }
}
