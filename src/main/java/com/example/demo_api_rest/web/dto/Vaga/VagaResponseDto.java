package com.example.demo_api_rest.web.dto.Vaga;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class VagaResponseDto {

    private Long id;
    private String status;
    private String codigo;
}
