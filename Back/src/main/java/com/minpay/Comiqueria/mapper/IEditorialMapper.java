package com.minpay.Comiqueria.mapper;

import com.minpay.Comiqueria.dto.EditorialRequestDTO;
import com.minpay.Comiqueria.dto.EditorialResponseDTO;
import com.minpay.Comiqueria.model.Editorial;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface IEditorialMapper {
    EditorialResponseDTO toEditorialResponseDTO(Editorial editorial);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    @Mapping(target = "productos", ignore = true)
    Editorial toEditorial(EditorialRequestDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    @Mapping(target = "productos", ignore = true)
    void updateEditorialFromDTO(EditorialRequestDTO dto, @MappingTarget Editorial editorial);
}