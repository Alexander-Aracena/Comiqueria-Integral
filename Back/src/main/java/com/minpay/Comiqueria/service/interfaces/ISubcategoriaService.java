package com.minpay.Comiqueria.service.interfaces;

import com.minpay.Comiqueria.dto.SubcategoriaRequestDTO;
import com.minpay.Comiqueria.dto.SubcategoriaResponseDTO;
import java.util.List;

public interface ISubcategoriaService {
    public SubcategoriaResponseDTO getSubcategoria(Long id);
    public List<SubcategoriaResponseDTO> getSubcategorias(
        List<Long> ids,
        String nombre,
        Long idCategoria,
        Boolean estaVigente
    );
    public SubcategoriaResponseDTO createSubcategoria(SubcategoriaRequestDTO subcategoriaDTO);
    public SubcategoriaResponseDTO editSubcategoria(Long id, SubcategoriaRequestDTO subcategoriaDTO);
    public void deleteSubcategoria(Long id);
}
