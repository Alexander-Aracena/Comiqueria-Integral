package com.minpay.Comiqueria.service.interfaces;

import com.minpay.Comiqueria.dto.ProductoRequestDTO;
import com.minpay.Comiqueria.dto.ProductoResponseDTO;
import java.math.BigDecimal;
import java.util.List;

public interface IProductoService {
    public ProductoResponseDTO getProducto(Long id);
    public List<ProductoResponseDTO> getProductos(
        List<Long> ids, String titulo, BigDecimal minPrecio, BigDecimal maxPrecio, String descripcion,
        Long idAutor, Long idSubcategoria, Long idEditorial, Boolean esNovedad, Boolean esOferta,
        Boolean esMasVendido, Boolean esVisibleEnHome, Boolean estaVigente
    );
    public ProductoResponseDTO createProducto(ProductoRequestDTO productoDTO);
    public ProductoResponseDTO editProducto(Long id, ProductoRequestDTO productoDTO);
    public void deleteProducto(Long id);
}