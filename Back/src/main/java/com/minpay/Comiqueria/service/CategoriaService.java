package com.minpay.Comiqueria.service;

import com.minpay.Comiqueria.dto.CategoriaRequestDTO;
import com.minpay.Comiqueria.dto.CategoriaResponseDTO;
import com.minpay.Comiqueria.exceptions.InvalidOperationException;
import com.minpay.Comiqueria.mapper.ICategoriaMapper;
import com.minpay.Comiqueria.service.interfaces.ICategoriaService;
import com.minpay.Comiqueria.model.Categoria;
import com.minpay.Comiqueria.model.Subcategoria;
import com.minpay.Comiqueria.repository.ICategoriaRepository;
import com.minpay.Comiqueria.repository.ISubcategoriaRepository;
import com.minpay.Comiqueria.repository.specification.CategoriaSpecifications;
import com.minpay.Comiqueria.utils.Utils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoriaService implements ICategoriaService {

    @Autowired
    private ICategoriaRepository categoriaRepository;
    
    @Autowired
    private ISubcategoriaRepository subcategoriaRepository;
    
    @Autowired
    private ICategoriaMapper categoriaMapper;

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO getCategoria(Long id) {
        Categoria categoria = Utils.findByIdOrThrow(categoriaRepository, id, Categoria.class);
        return this.categoriaMapper.toCategoriaResponseDTO(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> getCategorias(List<Long> ids, String nombre, Boolean estaVigente) {
        Specification<Categoria> specs = CategoriaSpecifications.byCriterios(ids, nombre, estaVigente);
        List<Categoria> categorias = this.categoriaRepository.findAll(specs);
        return Utils.mapearListaA(categorias, this.categoriaMapper::toCategoriaResponseDTO);
    }

    @Override
    public CategoriaResponseDTO createCategoria(CategoriaRequestDTO dto) {
        Categoria categoria = this.categoriaMapper.toCategoria(dto);
        categoria = this.categoriaRepository.save(categoria);
        return this.categoriaMapper.toCategoriaResponseDTO(categoria);
    }

    @Override
    public CategoriaResponseDTO editCategoria(Long id, CategoriaRequestDTO dto) {
        Categoria categoriaModificada = Utils.findByIdOrThrow(categoriaRepository, id, Categoria.class);
        if (!categoriaModificada.getEstaVigente()) {
            throw new InvalidOperationException("La categoría no está vigente");
        }
        this.categoriaMapper.updateCategoriaFromDTO(dto, categoriaModificada);
        categoriaModificada = this.categoriaRepository.save(categoriaModificada);
        return this.categoriaMapper.toCategoriaResponseDTO(categoriaModificada);
    }

    @Override
    public void deleteCategoria(Long id) {
        Categoria categoriaEliminada = Utils.findByIdOrThrow(categoriaRepository, id, Categoria.class);
        if (!categoriaEliminada.getEstaVigente()) {
            throw new InvalidOperationException("La categoría ya fue dada de baja");
        }
        categoriaEliminada.setFechaBaja(LocalDateTime.now());
        categoriaEliminada.setEstaVigente(Boolean.FALSE);
        this.categoriaRepository.save(categoriaEliminada);
    }

    @Override
    public void addSubcategories(Long idCategoria, Set<Long> idsSubcategorias) {
        Categoria categoria = Utils.findByIdOrThrow(categoriaRepository, idCategoria, Categoria.class);
        if (!categoria.getEstaVigente()) {
            throw new InvalidOperationException("La categoría no está vigente");
        }
        Set<Subcategoria> subcategorias = Utils.findAllById(subcategoriaRepository, idsSubcategorias);
        categoria.getSubcategorias().addAll(subcategorias);
        subcategorias.forEach(subcategoria -> subcategoria.setCategoria(categoria));
        this.subcategoriaRepository.saveAll(subcategorias);
    }

    @Override
    public void deleteSubcategories(Long idCategoria, Set<Long> idsSubcategorias) {
        Categoria categoria = Utils.findByIdOrThrow(categoriaRepository, idCategoria, Categoria.class);
        if (!categoria.getEstaVigente()) {
            throw new InvalidOperationException("La categoría no está vigente");
        }
        Set<Subcategoria> subcategorias = Utils.findAllById(subcategoriaRepository, idsSubcategorias);
        categoria.getSubcategorias().removeAll(subcategorias);
        subcategorias.forEach(subcategoria -> subcategoria.setCategoria(null));
        this.subcategoriaRepository.saveAll(subcategorias);
    }
}