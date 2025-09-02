package com.minpay.Comiqueria.mapper;

import com.minpay.Comiqueria.dto.RegisterRequestDTO;
import com.minpay.Comiqueria.dto.UsuarioRequestDTO;
import com.minpay.Comiqueria.dto.UsuarioResponseDTO;
import com.minpay.Comiqueria.model.Usuario;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface IUsuarioMapper {
    @Mapping(source = "cliente.id", target = "idClienteAsociado")
    UsuarioResponseDTO toUsuarioResponseDTO(Usuario usuario);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaActivo", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "ultimoLogin", ignore = true)
    @Mapping(target = "resetPasswordToken", ignore = true)
    @Mapping(target = "resetTokenExpirationDate", ignore = true)
    Usuario toUsuario(UsuarioRequestDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estaActivo", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "ultimoLogin", ignore = true)
    @Mapping(target = "resetPasswordToken", ignore = true)
    @Mapping(target = "resetTokenExpirationDate", ignore = true)
    void updateUsuarioFromDTO(UsuarioRequestDTO dto, @MappingTarget Usuario usuario);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true) // Se hashea en el servicio
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "ultimoLogin", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "resetPasswordToken", ignore = true)
    @Mapping(target = "resetTokenExpirationDate", ignore = true)
    @Mapping(target = "rol", expression = "java(com.minpay.Comiqueria.model.Rol.CLIENTE)")
    @Mapping(target = "estaActivo", expression = "java(java.lang.Boolean.TRUE)")
    Usuario toUsuario(RegisterRequestDTO dto);
}