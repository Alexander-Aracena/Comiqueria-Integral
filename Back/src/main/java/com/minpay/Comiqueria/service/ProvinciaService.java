package com.minpay.Comiqueria.service;

import com.minpay.Comiqueria.dto.ProvinciaRequestDTO;
import com.minpay.Comiqueria.dto.ProvinciaResponseDTO;
import com.minpay.Comiqueria.exceptions.InvalidOperationException;
import com.minpay.Comiqueria.mapper.IProvinciaMapper;
import com.minpay.Comiqueria.model.Pais;
import com.minpay.Comiqueria.model.Provincia;
import com.minpay.Comiqueria.service.interfaces.IProvinciaService;
import com.minpay.Comiqueria.repository.IPaisRepository;
import com.minpay.Comiqueria.repository.IProvinciaRepository;
import com.minpay.Comiqueria.repository.specification.ProvinciaSpecifications;
import com.minpay.Comiqueria.utils.Utils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProvinciaService implements IProvinciaService {

    @Autowired
    private IProvinciaRepository provinciaRepository;

    @Autowired
    private IPaisRepository paisRepository;
    
    @Autowired
    private IProvinciaMapper provinciaMapper;

    @Override
    @Transactional(readOnly = true)
    public ProvinciaResponseDTO getProvincia(Long id) {
        Provincia provincia = Utils.findByIdOrThrow(provinciaRepository, id, Provincia.class);
        return this.provinciaMapper.toProvinciaResponseDTO(provincia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvinciaResponseDTO> getProvincias(
        List<Long> ids,
        String nombre,
        Long idPais,
        Boolean estaVigente
    ) {
        Specification<Provincia> specs = ProvinciaSpecifications.byCriterios(
            ids, nombre, idPais, estaVigente
        );
        List<Provincia> provincias = this.provinciaRepository.findAll(specs);
        return Utils.mapearListaA(provincias, this.provinciaMapper::toProvinciaResponseDTO);
    }

    @Override
    public ProvinciaResponseDTO createProvincia(ProvinciaRequestDTO provinciaDTO) {
        Provincia provincia = this.provinciaMapper.toProvincia(provinciaDTO);
        Pais nuevoPais = Utils.findByIdOrThrow(paisRepository, provinciaDTO.getIdPais(), Pais.class);
        provincia.setPais(nuevoPais);
        provincia = this.provinciaRepository.save(provincia);
        return this.provinciaMapper.toProvinciaResponseDTO(provincia);
    }

    @Override
    public ProvinciaResponseDTO editProvincia(Long id, ProvinciaRequestDTO provinciaDTO) {
        Provincia provinciaModificada = Utils.findByIdOrThrow(provinciaRepository, id, Provincia.class);
        if (!provinciaModificada.getEstaVigente()) {
            throw new InvalidOperationException("La provincia no está vigente");
        }
        if (provinciaDTO.getIdPais() != null) {
            Pais nuevoPais = Utils.findByIdOrThrow(paisRepository, provinciaDTO.getIdPais(), Pais.class);
            if (!nuevoPais.getEstaVigente()) {
                throw new InvalidOperationException("El pais seleccionado no está vigente");
            }
            if (!Objects.equals(provinciaModificada.getPais().getId(), provinciaDTO.getIdPais())) {
                provinciaModificada.setPais(nuevoPais);
            }
        }
        this.provinciaMapper.updateProvinciaFromDTO(provinciaDTO, provinciaModificada);
        provinciaModificada = this.provinciaRepository.save(provinciaModificada);
        return this.provinciaMapper.toProvinciaResponseDTO(provinciaModificada);
    }

    @Override
    public void deleteProvincia(Long id) {
        Provincia provinciaEliminada = Utils.findByIdOrThrow(provinciaRepository, id, Provincia.class);
        if (!provinciaEliminada.getEstaVigente()) {
            throw new InvalidOperationException("La provincia ya fue dada de baja");
        }
        provinciaEliminada.setEstaVigente(Boolean.FALSE);
        provinciaEliminada.setFechaBaja(LocalDateTime.now());
        this.provinciaRepository.save(provinciaEliminada);
    }
}
