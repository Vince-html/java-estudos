package com.example.demo_api_rest.service;


import com.example.demo_api_rest.entity.Cliente;
import com.example.demo_api_rest.entity.ClienteVaga;
import com.example.demo_api_rest.entity.Vaga;
import com.example.demo_api_rest.util.EstacionamentoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class EstacionamentoService {

    private final ClienteVagaService clienteVagaService;
    private final ClientService clientService;
    private final VagaService vagaService;

    @Transactional
    public ClienteVaga checkIn (ClienteVaga clienteVaga) {

        Cliente cliente = clientService.findByCPF(clienteVaga.getCliente().getCpf());

        clienteVaga.setCliente(cliente);
        Vaga vaga = vagaService.findVagaLivre();
        vaga.setStatus(Vaga.StatusVagas.OCUPADO);

        clienteVaga.setVaga(vaga);

        clienteVaga.setDataEntrada(LocalDateTime.now());

        clienteVaga.setRecibo(EstacionamentoUtils.gerarRecibo());

        return clienteVagaService.salvar(clienteVaga);
    }

    @Transactional
    public ClienteVaga checkOut(String recibo) {
        ClienteVaga clienteVaga = clienteVagaService.findByRecibo(recibo);
        LocalDateTime dataSaida = LocalDateTime.now();

        BigDecimal valor = EstacionamentoUtils.calcularCusto(clienteVaga.getDataEntrada(), dataSaida);
        clienteVaga.setValor(valor);

        long totalDeVezes = clienteVagaService.getTotalDeVezesEstacionamentoCompleto(clienteVaga.getCliente().getCpf());

        BigDecimal desconto = EstacionamentoUtils.calcularDesconto(valor, totalDeVezes);

        clienteVaga.setDesconto(desconto);

        clienteVaga.setDataSaida(dataSaida);

        clienteVaga.getVaga().setStatus(Vaga.StatusVagas.LIVRE);

        return clienteVagaService.salvar(clienteVaga);
    }
}
