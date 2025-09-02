package com.minpay.Comiqueria.service;

import com.minpay.Comiqueria.dto.LoginRequestDTO;
import com.minpay.Comiqueria.dto.LoginResponseDTO;
import com.minpay.Comiqueria.dto.RegisterRequestDTO;
import com.minpay.Comiqueria.dto.UsuarioResponseDTO;
import com.minpay.Comiqueria.exceptions.InvalidOperationException;
import com.minpay.Comiqueria.exceptions.InvalidTokenException;
import com.minpay.Comiqueria.exceptions.ResourceAlreadyExistsException;
import com.minpay.Comiqueria.exceptions.ResourceNotFoundException;
import com.minpay.Comiqueria.jwt.JwtService;
import com.minpay.Comiqueria.mapper.IUsuarioMapper;
import com.minpay.Comiqueria.model.Rol;
import com.minpay.Comiqueria.model.Usuario;
import com.minpay.Comiqueria.repository.IUsuarioRepository;
import com.minpay.Comiqueria.service.interfaces.IAuthService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService implements IAuthService {
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUsuarioMapper usuarioMapper;

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getContrasenia()
            )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = this.usuarioRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException(
            "Error interno: Usuario no encontrado en la base de datos."
        ));
        
        String jwtToken = jwtService.generateToken(userDetails);
        usuario.setUltimoLogin(LocalDateTime.now());

        return new LoginResponseDTO(
            usuario.getId(),
            jwtToken,
            usuario.getEmail(),
            usuario.getRol().name()
        );
    }

    @Override
    public UsuarioResponseDTO register(RegisterRequestDTO request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException(
                "El email '" + request.getEmail() + "' ya está registrado."
            );
        }

        Usuario nuevoUsuario = this.usuarioMapper.toUsuario(request);
        nuevoUsuario.setPasswordHash(passwordEncoder.encode(request.getContrasenia()));
        nuevoUsuario.setRol(Rol.CLIENTE);
        nuevoUsuario.setEstaActivo(Boolean.TRUE);
        Usuario usuarioGuardado = this.usuarioRepository.save(nuevoUsuario);

        return this.usuarioMapper.toUsuarioResponseDTO(usuarioGuardado);
    }

    @Override
    public String forgotPassword(String email) {
        Usuario usuario = this.usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException(
            "No se encontró un usuario con el email proporcionado."
        ));

        // 2. Generar un token de restablecimiento único y una fecha de expiración (1 hora)
        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expirationDate = LocalDateTime.now().plusHours(1);

        // 3. Almacenar el token y la fecha de expiración en el usuario
        usuario.setResetPasswordToken(resetToken);
        usuario.setResetTokenExpirationDate(expirationDate);
        this.usuarioRepository.save(usuario);
        
        return resetToken;
    }

    @Override
    public UsuarioResponseDTO resetPassword(String token, String newPassword) {
        Usuario usuario = usuarioRepository.findByResetPasswordToken(token)
            .orElseThrow(() -> new InvalidTokenException(
            "Token de restablecimiento inválido o no encontrado."
        ));

        // 2. Verificar si el token ha expirado
        if (usuario.getResetTokenExpirationDate() == null ||
            usuario.getResetTokenExpirationDate().isBefore(LocalDateTime.now())) {
            usuario.setResetPasswordToken(null);
            usuario.setResetTokenExpirationDate(null);
            usuarioRepository.save(usuario); // Guardar cambios para limpiar el token
            throw new InvalidTokenException("El token de restablecimiento ha expirado.");
        }

        // 3. Cifrar la nueva contraseña y actualizarla
        if (passwordEncoder.matches(newPassword, usuario.getPasswordHash())) {
            throw new InvalidOperationException("La nueva contraseña no puede ser igual a la anterior.");
        }
        usuario.setPasswordHash(passwordEncoder.encode(newPassword));

        // 4. Limpiar el token de restablecimiento y su fecha de expiración (para evitar reutilización)
        usuario.setResetPasswordToken(null);
        usuario.setResetTokenExpirationDate(null);

        // 5. Guardar los cambios en el usuario
        Usuario usuarioGuardado = this.usuarioRepository.save(usuario);

        return this.usuarioMapper.toUsuarioResponseDTO(usuarioGuardado);
    }

    @Override
    public LoginResponseDTO forceChangePassword(String email, String newPassword) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        if (usuario.getResetPasswordToken() == null ||
            !usuario.getResetPasswordToken().equals("INITIAL_SETUP")) {
            throw new IllegalArgumentException("No se requiere cambio de contraseña forzado para este usuario.");
        }
        
        if (passwordEncoder.matches(newPassword, usuario.getPasswordHash())) {
            throw new InvalidOperationException("La nueva contraseña no puede ser igual a la anterior.");
        }

        usuario.setPasswordHash(passwordEncoder.encode(newPassword));
        usuario.setResetPasswordToken(null);
        usuario.setResetTokenExpirationDate(null);
        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                usuario.getEmail(),
                newPassword
            )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtService.generateToken(userDetails);
        return new LoginResponseDTO(
            usuario.getId(),
            jwt,
            usuario.getEmail(),
            usuario.getRol().name()
        );
    }
}
