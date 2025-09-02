package com.minpay.Comiqueria.service.interfaces;

import com.minpay.Comiqueria.dto.UsuarioRequestDTO;
import com.minpay.Comiqueria.dto.UsuarioResponseDTO;
import com.minpay.Comiqueria.model.Rol;
import java.util.List;

public interface IUsuarioService {
    public UsuarioResponseDTO getUsuario(Long id);
    public List<UsuarioResponseDTO> getUsuarios(
        List<Long> ids, String email, Rol rol, Long idCliente, Boolean estaActivo
    );
    public UsuarioResponseDTO createUsuario(UsuarioRequestDTO usuarioDTO);
    public UsuarioResponseDTO editUsuario(Long id, UsuarioRequestDTO usuarioDTO);
    public void deleteUsuario(Long id);
}
