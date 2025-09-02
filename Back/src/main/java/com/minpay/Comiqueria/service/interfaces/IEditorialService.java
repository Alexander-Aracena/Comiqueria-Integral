package com.minpay.Comiqueria.service.interfaces;

import com.minpay.Comiqueria.dto.EditorialRequestDTO;
import com.minpay.Comiqueria.dto.EditorialResponseDTO;
import java.util.List;

public interface IEditorialService {
    public EditorialResponseDTO getEditorial(Long id);
    public List<EditorialResponseDTO> getEditoriales(List<Long> ids, String nombre, Boolean estaVigente);
    public EditorialResponseDTO createEditorial(EditorialRequestDTO editorialRequestDTO);
    public EditorialResponseDTO editEditorial(Long id, EditorialRequestDTO editorialRequestDTO);
    public void deleteEditorial(Long id);
}
