package com.example.demo_api_rest.repository.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ClienteVagaProjection {
    String getPlaca();

    String getMarca();

    String getModelo();

    String getCor();

    String getClienteCpf();

    String getRecibo();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime getDataEntrada();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime getDataSaida();

    String getVagaCodigo();

    BigDecimal getValor();

    BigDecimal getDesconto();
}
