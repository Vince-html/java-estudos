package com.example.demo_api_rest.service;


import com.example.demo_api_rest.entity.ClienteVaga;
import com.example.demo_api_rest.exception.EntityNotFoundException;
import com.example.demo_api_rest.repository.ClienteVagaRepository;
import com.example.demo_api_rest.repository.projection.ClienteVagaProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClienteVagaService {
    private final ClienteVagaRepository clienteVagaRepository;

    @Transactional
    public ClienteVaga salvar (ClienteVaga clienteVaga){
        return clienteVagaRepository.save(clienteVaga);
    }

    @Transactional(readOnly = true)
    public ClienteVaga findByRecibo(String recibo) {
        return clienteVagaRepository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(
                () -> new EntityNotFoundException("Recibo não encontrado ou já fechado.")
        );
    }

    @Transactional(readOnly = true)
    public long getTotalDeVezesEstacionamentoCompleto(String cpf) {
        return clienteVagaRepository.countByClienteCpfAndDataSaidaIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> buscarTodosPorClienteCPF(String cpf, Pageable pageable) {
        return clienteVagaRepository.findAllByClienteCpf(cpf, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> buscarTodosPorUsuarioId(Long id, Pageable pageable) {
        return clienteVagaRepository.findAllByClienteUsuarioId(id, pageable);
    }
}
