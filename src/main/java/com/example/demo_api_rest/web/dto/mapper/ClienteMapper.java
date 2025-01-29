package com.example.demo_api_rest.web.dto.mapper;


import com.example.demo_api_rest.entity.Client;
import com.example.demo_api_rest.web.dto.Cliente.ClienteCreateDto;
import com.example.demo_api_rest.web.dto.Cliente.ClienteResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Client toCliente(ClienteCreateDto dto) {
        return new ModelMapper().map(dto, Client.class);
    }

    public static ClienteResponseDto toClienteDto(Client client) {
        return new ModelMapper().map(client, ClienteResponseDto.class);
    }

}
