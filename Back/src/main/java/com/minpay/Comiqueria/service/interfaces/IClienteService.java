package com.minpay.Comiqueria.service.interfaces;

import com.minpay.Comiqueria.dto.ClienteRequestDTO;
import com.minpay.Comiqueria.dto.ClienteResponseDTO;
import java.util.List;
import java.util.Set;

public interface IClienteService {
    public ClienteResponseDTO getCliente(Long id);
    public List<ClienteResponseDTO> getClientes(
        List<Long> ids, String nombre, String apellido, String nroDoc, Boolean estaVigente
    );
    public ClienteResponseDTO createCliente(ClienteRequestDTO clienteDTO);
    public ClienteResponseDTO editCliente(Long id, ClienteRequestDTO clienteDTO);
    public void deleteCliente(Long id);
    public void addFavoritos(Long idCliente, Set<Long> idsProductos);
    public void deleteFavoritos(Long idCliente, Set<Long> idsProductos);
    public void addDomicilio(Long idCliente, Long idDomicilio);
    public void deleteDomicilio(Long idCliente, Long idDomicilio);
}
