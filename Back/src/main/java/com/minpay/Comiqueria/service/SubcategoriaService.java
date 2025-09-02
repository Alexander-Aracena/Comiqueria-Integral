package com.minpay.Comiqueria.service;

import com.minpay.Comiqueria.dto.SubcategoriaRequestDTO;
import com.minpay.Comiqueria.dto.SubcategoriaResponseDTO;
import com.minpay.Comiqueria.exceptions.InvalidOperationException;
import com.minpay.Comiqueria.mapper.ISubcategoriaMapper;
import com.minpay.Comiqueria.model.Categoria;
import com.minpay.Comiqueria.service.interfaces.ISubcategoriaService;
import com.minpay.Comiqueria.model.Subcategoria;
import com.minpay.Comiqueria.repository.ICategoriaRepository;
import com.minpay.Comiqueria.repository.ISubcategoriaRepository;
import com.minpay.Comiqueria.repository.specification.SubcategoriaSpecifications;
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
public class SubcategoriaService implements ISubcategoriaService {

    @Autowired
    private ISubcategoriaRepository subcategoriaRepository;

    @Autowired
    private ISubcategoriaMapper subcategoriaMapper;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public SubcategoriaResponseDTO getSubcategoria(Long id) {
        Subcategoria subcategoria = Utils.findByIdOrThrow(subcategoriaRepository, id, Subcategoria.class);
        return this.subcategoriaMapper.toSubcategoriaResponseDTO(subcategoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubcategoriaResponseDTO> getSubcategorias(List<Long> ids, String nombre, Long idCategoria, Boolean estaVigente) {
        Specification<Subcategoria> specs = SubcategoriaSpecifications.byCriterios(
            ids, nombre, idCategoria, estaVigente
        );
        List<Subcategoria> subcategorias = this.subcategoriaRepository.findAll(specs);
        return Utils.mapearListaA(subcategorias, this.subcategoriaMapper::toSubcategoriaResponseDTO);
    }

    @Override
    public SubcategoriaResponseDTO createSubcategoria(SubcategoriaRequestDTO subcategoriaDTO) {
        Subcategoria subcategoria = this.subcategoriaMapper.toSubcategoria(subcategoriaDTO);
        Categoria categoria = Utils.findByIdOrThrow(
            categoriaRepository, subcategoriaDTO.getIdCategoria(), Categoria.class
        );
        if (!categoria.getEstaVigente()) {
            throw new InvalidOperationException("La categoría seleccionada ya no está vigente");
        }
        subcategoria.setCategoria(categoria);
        categoria.getSubcategorias().add(subcategoria);
        subcategoria = this.subcategoriaRepository.save(subcategoria);
        return this.subcategoriaMapper.toSubcategoriaResponseDTO(subcategoria);
    }

    @Override
    public SubcategoriaResponseDTO editSubcategoria(Long id, SubcategoriaRequestDTO subcategoriaDTO) {
        Subcategoria subcategoriaModificada = Utils.findByIdOrThrow(
            subcategoriaRepository, id, Subcategoria.class
        );
        if (!subcategoriaModificada.getEstaVigente()) {
            throw new InvalidOperationException("La subcategoría no está vigente");
        }
        if (subcategoriaDTO.getIdCategoria() != null) {
            Categoria nuevaCategoria = Utils.findByIdOrThrow(
                categoriaRepository, subcategoriaDTO.getIdCategoria(), Categoria.class
            );
            Categoria categoriaAnterior = subcategoriaModificada.getCategoria();
            if (!nuevaCategoria.getEstaVigente()) {
                throw new InvalidOperationException("La categoría seleccionada ya no está vigente");
            }
            if (!Objects.equals(subcategoriaModificada.getCategoria().getId(), nuevaCategoria.getId())) {
                categoriaAnterior.getSubcategorias().remove(subcategoriaModificada);
                subcategoriaModificada.setCategoria(nuevaCategoria);
                nuevaCategoria.getSubcategorias().add(subcategoriaModificada);
            }
        }
        this.subcategoriaMapper.updateSubcategoriaFromDTO(subcategoriaDTO, subcategoriaModificada);
        subcategoriaModificada = this.subcategoriaRepository.save(subcategoriaModificada);
        return this.subcategoriaMapper.toSubcategoriaResponseDTO(subcategoriaModificada);
    }

    @Override
    public void deleteSubcategoria(Long id) {
        Subcategoria subcategoriaEliminada = Utils.findByIdOrThrow(
            subcategoriaRepository, id, Subcategoria.class
        );
        if (!subcategoriaEliminada.getEstaVigente()) {
            throw new InvalidOperationException("La subcategoría ya fue dada de baja");
        }
        subcategoriaEliminada.setEstaVigente(Boolean.FALSE);
        subcategoriaEliminada.setFechaBaja(LocalDateTime.now());
        this.subcategoriaRepository.save(subcategoriaEliminada);
    }
}