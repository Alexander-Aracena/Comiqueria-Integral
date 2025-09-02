package com.minpay.Comiqueria.service.interfaces;

import com.minpay.Comiqueria.dto.DomicilioRequestDTO;
import com.minpay.Comiqueria.dto.DomicilioResponseDTO;
import com.minpay.Comiqueria.model.Domicilio;
import java.util.List;
import java.util.Set;

public interface IDomicilioService {
    public DomicilioResponseDTO getDomicilio(Long id);
    public List<DomicilioResponseDTO> getDomicilios(
        List<Long> ids,
        String calle,
        String cp,
        Boolean estaVigente
    );
    public DomicilioResponseDTO createDomicilio(DomicilioRequestDTO domicilioDTO);
    public DomicilioResponseDTO editDomicilio(Long id, DomicilioRequestDTO domicilioDTO);
    public void deleteDomicilio(Long id);
}