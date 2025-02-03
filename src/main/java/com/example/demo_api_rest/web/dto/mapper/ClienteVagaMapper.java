package com.example.demo_api_rest.web.dto.mapper;


import com.example.demo_api_rest.entity.ClienteVaga;
import com.example.demo_api_rest.web.dto.Estacionamento.EstacionamentoCreateDto;
import com.example.demo_api_rest.web.dto.Estacionamento.EstacionamentoResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteVagaMapper {

    public static ClienteVaga toClienteVaga(EstacionamentoCreateDto dto) {
        return new ModelMapper().map(dto, ClienteVaga.class);
    }

    public static EstacionamentoResponseDto toClienteVagaDto(ClienteVaga clienteVaga){
        return new ModelMapper().map(clienteVaga, EstacionamentoResponseDto.class);
    }
}
