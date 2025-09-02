package com.minpay.Comiqueria.service.interfaces;

import com.minpay.Comiqueria.dto.PaisRequestDTO;
import com.minpay.Comiqueria.dto.PaisResponseDTO;
import java.util.List;

public interface IPaisService {
    public PaisResponseDTO getPais(Long id);
    public List<PaisResponseDTO> getPaises(List<Long> ids, String nombre, Boolean estaVigente);
    public PaisResponseDTO createPais(PaisRequestDTO paisDTO);
    public PaisResponseDTO editPais(Long id, PaisRequestDTO paisDTO);
    public void deletePais(Long id);
}