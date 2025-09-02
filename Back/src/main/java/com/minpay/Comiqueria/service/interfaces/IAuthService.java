package com.minpay.Comiqueria.service.interfaces;

import com.minpay.Comiqueria.dto.LoginRequestDTO;
import com.minpay.Comiqueria.dto.LoginResponseDTO;
import com.minpay.Comiqueria.dto.RegisterRequestDTO;
import com.minpay.Comiqueria.dto.UsuarioResponseDTO;

public interface IAuthService {
    LoginResponseDTO login(LoginRequestDTO request);
    UsuarioResponseDTO register(RegisterRequestDTO request);
    String forgotPassword(String email);
    UsuarioResponseDTO resetPassword(String token, String newPassword);
    LoginResponseDTO forceChangePassword(String nombreUsuario, String nuevaContrasenia);
}
