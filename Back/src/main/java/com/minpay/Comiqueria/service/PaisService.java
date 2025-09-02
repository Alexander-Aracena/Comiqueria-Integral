package com.minpay.Comiqueria.service;

import com.minpay.Comiqueria.dto.PaisRequestDTO;
import com.minpay.Comiqueria.dto.PaisResponseDTO;
import com.minpay.Comiqueria.exceptions.InvalidOperationException;
import com.minpay.Comiqueria.mapper.IPaisMapper;
import com.minpay.Comiqueria.service.interfaces.IPaisService;
import com.minpay.Comiqueria.model.Pais;
import com.minpay.Comiqueria.repository.IPaisRepository;
import com.minpay.Comiqueria.repository.specification.PaisSpecifications;
import com.minpay.Comiqueria.utils.Utils;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaisService implements IPaisService {

    @Autowired
    private IPaisRepository paisRepository;
    
    @Autowired
    private IPaisMapper paisMapper;

    @Override
    @Transactional(readOnly = true)
    public PaisResponseDTO getPais(Long id) {
        Pais pais = Utils.findByIdOrThrow(paisRepository, id, Pais.class);
        return this.paisMapper.toPaisResponseDTO(pais);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaisResponseDTO> getPaises(List<Long> ids, String nombre, Boolean estaVigente) {
        Specification<Pais> specs = PaisSpecifications.byCriterios(ids, nombre, estaVigente);
        List<Pais> paises = this.paisRepository.findAll(specs);
        return Utils.mapearListaA(paises, this.paisMapper::toPaisResponseDTO);
    }

    @Override
    public PaisResponseDTO createPais(PaisRequestDTO paisDTO) {
        Pais pais = this.paisMapper.toPais(paisDTO);
        pais = this.paisRepository.save(pais);
        return this.paisMapper.toPaisResponseDTO(pais);
    }

    @Override
    public PaisResponseDTO editPais(Long id, PaisRequestDTO paisDTO) {
        Pais paisModificado = Utils.findByIdOrThrow(paisRepository, id, Pais.class);
        if (!paisModificado.getEstaVigente()) {
            throw new InvalidOperationException("El pais ya no está vigente");
        }
        this.paisMapper.updatePaisFromDTO(paisDTO, paisModificado);
        paisModificado = this.paisRepository.save(paisModificado);
        return this.paisMapper.toPaisResponseDTO(paisModificado);
    }

    @Override
    public void deletePais(Long id) {
        Pais paisEliminado = Utils.findByIdOrThrow(paisRepository, id, Pais.class);
        if (!paisEliminado.getEstaVigente()) {
            throw new InvalidOperationException("El pais ya no está vigente");
        }
        paisEliminado.setFechaBaja(LocalDateTime.now());
        paisEliminado.setEstaVigente(Boolean.FALSE);
        this.paisRepository.save(paisEliminado);
    }
}
