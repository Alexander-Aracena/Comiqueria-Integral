package com.minpay.Comiqueria.mapper;

import com.minpay.Comiqueria.dto.PaisRequestDTO;
import com.minpay.Comiqueria.dto.PaisResponseDTO;
import com.minpay.Comiqueria.model.Pais;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface IPaisMapper {
    PaisResponseDTO toPaisResponseDTO(Pais pais);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "provincias", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    Pais toPais(PaisRequestDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "provincias", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    void updatePaisFromDTO(PaisRequestDTO dto, @MappingTarget Pais pais);
}