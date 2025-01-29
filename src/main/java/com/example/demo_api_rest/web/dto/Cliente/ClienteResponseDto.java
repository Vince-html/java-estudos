package com.example.demo_api_rest.web.dto.Cliente;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClienteResponseDto {
    private Long id;
    private String nome;
    private String cpf;
}
