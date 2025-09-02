package com.minpay.Comiqueria.mapper;

import com.minpay.Comiqueria.dto.CategoriaRequestDTO;
import com.minpay.Comiqueria.dto.CategoriaResponseDTO;
import com.minpay.Comiqueria.model.Categoria;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ICategoriaMapper {
    CategoriaResponseDTO toCategoriaResponseDTO(Categoria categoria);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    @Mapping(target = "subcategorias", ignore = true)
    Categoria toCategoria(CategoriaRequestDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    @Mapping(target = "subcategorias", ignore = true)
    void updateCategoriaFromDTO(CategoriaRequestDTO dto, @MappingTarget Categoria categoria);
}