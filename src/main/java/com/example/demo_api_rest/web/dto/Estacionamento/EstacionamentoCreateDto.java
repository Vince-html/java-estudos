package com.example.demo_api_rest.web.dto.Estacionamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstacionamentoCreateDto {
    @NotBlank
    @Size(min=8, max=8)
    @Pattern(regexp = "[A-Z]{3}-[0-9]{1}[A-Z/0-9]{1}[0-9]{2}", message = "A placa pode ser no formato antigo xxx-0000 ou no formato mercosul xxx-0x00")
    private String placa;
    @NotBlank
    private String marca;
    @NotBlank
    private String modelo;
    @NotBlank
    private String cor;
    @NotBlank
    @Size(min = 11, max = 11)
    @CPF
    private String clienteCpf;
}
