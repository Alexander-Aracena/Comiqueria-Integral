package com.minpay.Comiqueria.mapper;

import com.minpay.Comiqueria.dto.CarruselRequestDTO;
import com.minpay.Comiqueria.dto.CarruselResponseDTO;
import com.minpay.Comiqueria.model.Carrusel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ICarruselMapper {
    CarruselResponseDTO toCarruselResponseDTO(Carrusel carrusel);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orden", ignore = true)
    @Mapping(target = "estaActivo", ignore = true)
    Carrusel toCarrusel(CarruselRequestDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orden", ignore = true)
    @Mapping(target = "estaActivo", ignore = true)
    void updateCarruselFromDTO(CarruselRequestDTO dto, @MappingTarget Carrusel carrusel);
}
