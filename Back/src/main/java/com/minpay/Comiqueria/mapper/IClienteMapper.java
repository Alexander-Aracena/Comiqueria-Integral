package com.minpay.Comiqueria.mapper;

import com.minpay.Comiqueria.dto.ClienteRequestDTO;
import com.minpay.Comiqueria.dto.ClienteResponseDTO;
import com.minpay.Comiqueria.model.Cliente;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {IUsuarioMapper.class, IVentaMapper.class, IProductoMapper.class})
public interface IClienteMapper {
    @Mapping(target = "ventasRecientes", source = "ventas")
    ClienteResponseDTO toClienteResponseDTO(Cliente cliente);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "domicilios", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    @Mapping(target = "favoritos", ignore = true)
    @Mapping(target = "ventas", ignore = true)
    Cliente toCliente(ClienteRequestDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "domicilios", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaVigente", ignore = true)
    @Mapping(target = "favoritos", ignore = true)
    @Mapping(target = "ventas", ignore = true)
    void updateClienteFromDTO(ClienteRequestDTO dto, @MappingTarget Cliente cliente);
}