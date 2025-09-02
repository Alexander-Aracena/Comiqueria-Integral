package com.minpay.Comiqueria.mapper;

import com.minpay.Comiqueria.dto.DepartamentoRequestDTO;
import com.minpay.Comiqueria.dto.DepartamentoResponseDTO;
import com.minpay.Comiqueria.model.Departamento;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface IDepartamentoMapper {
    DepartamentoResponseDTO toDepartamentoResponseDTO(Departamento departamento);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "provincia", ignore = true)
    @Mapping(target = "localidades", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    Departamento toDepartamento(DepartamentoRequestDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "provincia", ignore = true)
    @Mapping(target = "localidades", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    void updateDepartamentoFromDTO(DepartamentoRequestDTO dto, @MappingTarget Departamento departamento);
}