package com.minpay.Comiqueria.mapper;

import com.minpay.Comiqueria.dto.AutorRequestDTO;
import com.minpay.Comiqueria.dto.AutorResponseDTO;
import com.minpay.Comiqueria.model.Autor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface IAutorMapper {
    AutorResponseDTO toAutorResponseDTO(Autor autor);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    Autor toAutor(AutorRequestDTO autorRequestDTO);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    void updateAutorFromDTO(AutorRequestDTO autorRequestDTO, @MappingTarget Autor autor);
}
