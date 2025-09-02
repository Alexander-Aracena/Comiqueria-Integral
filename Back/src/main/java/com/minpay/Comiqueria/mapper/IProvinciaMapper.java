package com.minpay.Comiqueria.mapper;

import com.minpay.Comiqueria.dto.ProvinciaRequestDTO;
import com.minpay.Comiqueria.dto.ProvinciaResponseDTO;
import com.minpay.Comiqueria.model.Provincia;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface IProvinciaMapper {
    ProvinciaResponseDTO toProvinciaResponseDTO(Provincia provincia);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departamentos", ignore = true)
    @Mapping(target = "pais", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    Provincia toProvincia(ProvinciaRequestDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departamentos", ignore = true)
    @Mapping(target = "pais", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    void updateProvinciaFromDTO(ProvinciaRequestDTO dto, @MappingTarget Provincia provincia);
}