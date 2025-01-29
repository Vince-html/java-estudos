package com.example.demo_api_rest.web.dto.mapper;


import com.example.demo_api_rest.entity.Vaga;
import com.example.demo_api_rest.web.dto.Vaga.VagaCreateDto;
import com.example.demo_api_rest.web.dto.Vaga.VagaResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VagaMapper {
    public static Vaga toVaga(VagaCreateDto dto) {
        return new ModelMapper().map(dto, Vaga.class);
    }

    public static VagaResponseDto toDto(Vaga vaga) {
        return new ModelMapper().map(vaga, VagaResponseDto.class);
    }
}
