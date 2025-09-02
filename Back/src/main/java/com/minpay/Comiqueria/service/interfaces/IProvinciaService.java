package com.minpay.Comiqueria.service.interfaces;

import com.minpay.Comiqueria.dto.ProvinciaRequestDTO;
import com.minpay.Comiqueria.dto.ProvinciaResponseDTO;
import java.util.List;

public interface IProvinciaService {
    public ProvinciaResponseDTO getProvincia(Long id);
    public List<ProvinciaResponseDTO> getProvincias(
        List<Long> ids,
        String nombre,
        Long idPais,
        Boolean estaVigente
    );
    public ProvinciaResponseDTO createProvincia(ProvinciaRequestDTO provinciaDTO);
    public ProvinciaResponseDTO editProvincia(Long id, ProvinciaRequestDTO provinciaDTO);
    public void deleteProvincia(Long id);
}