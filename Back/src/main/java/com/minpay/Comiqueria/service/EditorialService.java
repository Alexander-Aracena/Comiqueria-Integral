package com.minpay.Comiqueria.service;

import com.minpay.Comiqueria.dto.EditorialRequestDTO;
import com.minpay.Comiqueria.dto.EditorialResponseDTO;
import com.minpay.Comiqueria.exceptions.InvalidOperationException;
import com.minpay.Comiqueria.mapper.IEditorialMapper;
import com.minpay.Comiqueria.service.interfaces.IEditorialService;
import com.minpay.Comiqueria.model.Editorial;
import com.minpay.Comiqueria.repository.IEditorialRepository;
import com.minpay.Comiqueria.repository.specification.EditorialSpecifications;
import com.minpay.Comiqueria.utils.Utils;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EditorialService implements IEditorialService {
    
    @Autowired
    private IEditorialRepository editorialRepository;
    
    @Autowired
    private IEditorialMapper editorialMapper;

    @Override
    @Transactional(readOnly = true)
    public EditorialResponseDTO getEditorial(Long id) {
        Editorial editorial = Utils.findByIdOrThrow(editorialRepository, id, Editorial.class);
        return this.editorialMapper.toEditorialResponseDTO(editorial);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EditorialResponseDTO> getEditoriales(List<Long> ids, String nombre, Boolean estaVigente) {
        Specification<Editorial> specs = EditorialSpecifications.byCriterios(ids, nombre, estaVigente);
        List<Editorial> editoriales = this.editorialRepository.findAll(specs);
        return Utils.mapearListaA(editoriales, this.editorialMapper::toEditorialResponseDTO);
    }

    @Override
    public EditorialResponseDTO createEditorial(EditorialRequestDTO editorialRequestDTO) {
        Editorial editorial = this.editorialMapper.toEditorial(editorialRequestDTO);
        editorial = this.editorialRepository.save(editorial);
        return this.editorialMapper.toEditorialResponseDTO(editorial);
    }

    @Override
    public EditorialResponseDTO editEditorial(Long id, EditorialRequestDTO editorialRequestDTO) {
        Editorial editorialModificada = Utils.findByIdOrThrow(editorialRepository, id, Editorial.class);
        if (!editorialModificada.getEstaVigente()) {
            throw new InvalidOperationException("La editorial no está vigente");
        }
        this.editorialMapper.updateEditorialFromDTO(editorialRequestDTO, editorialModificada);
        editorialModificada = this.editorialRepository.save(editorialModificada);
        return this.editorialMapper.toEditorialResponseDTO(editorialModificada);
    }

    @Override
    public void deleteEditorial(Long id) {
        Editorial editorialEliminada = Utils.findByIdOrThrow(editorialRepository, id, Editorial.class);
        if (!editorialEliminada.getEstaVigente()) {
            throw new InvalidOperationException("La editorial no está vigente");
        }
        editorialEliminada.setFechaBaja(LocalDateTime.now());
        editorialEliminada.setEstaVigente(Boolean.FALSE);
        this.editorialRepository.save(editorialEliminada);
    }
}