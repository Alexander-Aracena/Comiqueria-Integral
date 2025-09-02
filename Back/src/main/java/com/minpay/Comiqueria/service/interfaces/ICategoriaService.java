package com.minpay.Comiqueria.service.interfaces;

import com.minpay.Comiqueria.dto.CategoriaRequestDTO;
import com.minpay.Comiqueria.dto.CategoriaResponseDTO;
import java.util.List;
import java.util.Set;

public interface ICategoriaService {
    public CategoriaResponseDTO getCategoria(Long id);
    public List<CategoriaResponseDTO> getCategorias(
        List<Long> ids,
        String nombre,
        Boolean estaVigente
    );
    public CategoriaResponseDTO createCategoria(CategoriaRequestDTO dto);
    public CategoriaResponseDTO editCategoria(Long id, CategoriaRequestDTO dto);
    public void deleteCategoria(Long id);
    public void addSubcategories(Long idCategoria, Set<Long> idsSubcategorias);
    public void deleteSubcategories(Long idCategoria, Set<Long> idsSubcategorias);
}
