package com.minpay.Comiqueria.mapper;

import com.minpay.Comiqueria.dto.VentaRequestDTO;
import com.minpay.Comiqueria.dto.VentaResponseDTO;
import com.minpay.Comiqueria.model.Venta;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {ILineaVentaMapper.class})
public interface IVentaMapper {
    VentaResponseDTO toVentaResponseDTO(Venta venta);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaVenta", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "lineas", ignore = true)
    Venta toVenta(VentaRequestDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaVenta", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "lineas", ignore = true)
    void updateVentaFromDTO(VentaRequestDTO dto, @MappingTarget Venta venta);
}