package com.minpay.Comiqueria.service.interfaces;

import com.minpay.Comiqueria.dto.AutorRequestDTO;
import com.minpay.Comiqueria.dto.AutorResponseDTO;
import java.util.List;
import java.util.Set;

public interface IAutorService {
    public AutorResponseDTO getAutor(Long id);
    public List<AutorResponseDTO> getAutores(List<Long> ids, String nombre, String apellido, Boolean estaVigente);
    public AutorResponseDTO createAutor(AutorRequestDTO autorDTO);
    public AutorResponseDTO editAutor(Long id, AutorRequestDTO autorDTO);
    public void deleteAutor(Long id);
    public void addProductos(Long idAutor, Set<Long> idsProductos);
    public void deleteProductos(Long idAutor, Set<Long> idsProductos);
}