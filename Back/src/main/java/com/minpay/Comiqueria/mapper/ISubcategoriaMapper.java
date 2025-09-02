package com.minpay.Comiqueria.mapper;

import com.minpay.Comiqueria.dto.SubcategoriaRequestDTO;
import com.minpay.Comiqueria.dto.SubcategoriaResponseDTO;
import com.minpay.Comiqueria.model.Subcategoria;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ISubcategoriaMapper {
    SubcategoriaResponseDTO toSubcategoriaResponseDTO(Subcategoria subcategoria);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    @Mapping(target = "productos", ignore = true)
    Subcategoria toSubcategoria(SubcategoriaRequestDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    @Mapping(target = "productos", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    void updateSubcategoriaFromDTO(SubcategoriaRequestDTO dto, @MappingTarget Subcategoria subcategoria);
}