package com.example.demo_api_rest.web.dto.mapper;

import com.example.demo_api_rest.entity.Usuario;
import com.example.demo_api_rest.web.dto.Usuarios.UsuarioCreateDto;
import com.example.demo_api_rest.web.dto.Usuarios.UsuarioResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {
    public static Usuario toUsuario(UsuarioCreateDto createDto){
        return new ModelMapper().map(createDto, Usuario.class);
    }

    public static UsuarioResponseDto toDto(Usuario usuario){
        String role = usuario.getRole().name().substring("ROLE_".length());
        ModelMapper mapperMain = new ModelMapper();
        TypeMap<Usuario, UsuarioResponseDto> propertyMapper = mapperMain.createTypeMap(Usuario.class, UsuarioResponseDto.class);
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> role, UsuarioResponseDto::setRole)
        );
        return mapperMain.map(usuario, UsuarioResponseDto.class);
    }

    public static List<UsuarioResponseDto> toListDto(List<Usuario> usuarios) {
        return usuarios.stream().map(UsuarioMapper::toDto).collect(Collectors.toList());
    }

}
