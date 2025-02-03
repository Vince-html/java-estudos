package com.example.demo_api_rest.web.dto.mapper;


import com.example.demo_api_rest.entity.Cliente;
import com.example.demo_api_rest.web.dto.Cliente.ClienteCreateDto;
import com.example.demo_api_rest.web.dto.Cliente.ClienteResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDto toClienteDto(Cliente Cliente) {
        return new ModelMapper().map(Cliente, ClienteResponseDto.class);
    }

}
