package com.minpay.Comiqueria.mapper;

import com.minpay.Comiqueria.dto.ProductoRequestDTO;
import com.minpay.Comiqueria.dto.ProductoResponseDTO;
import com.minpay.Comiqueria.model.Producto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface IProductoMapper {
    ProductoResponseDTO toProductoResponseDTO(Producto producto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "autores", ignore = true)
    @Mapping(target = "subcategoria", ignore = true)
    @Mapping(target = "editorial", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    @Mapping(target = "clientes", ignore = true)
    @Mapping(target = "lineasVenta", ignore = true)
    Producto toProducto(ProductoRequestDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "autores", ignore = true)
    @Mapping(target = "subcategoria", ignore = true)
    @Mapping(target = "editorial", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    @Mapping(target = "clientes", ignore = true)
    @Mapping(target = "lineasVenta", ignore = true)
    void updateProductoFromDTO(ProductoRequestDTO dto, @MappingTarget Producto producto);
}