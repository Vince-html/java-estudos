package com.example.demo_api_rest.web.dto.Estacionamento;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EstacionamentoResponseDto {

    private String placa;

    private String marca;

    private String modelo;

    private String cor;

    private String clienteCpf;

    private String recibo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataEntrada;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataSaida;

    private String vagaCodigo;

    private BigDecimal valor;

    private BigDecimal desconto;
}
