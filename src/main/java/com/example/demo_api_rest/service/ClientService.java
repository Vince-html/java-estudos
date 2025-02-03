package com.example.demo_api_rest.service;

import com.example.demo_api_rest.entity.Cliente;
import com.example.demo_api_rest.exception.CpfUniqueViolationException;
import com.example.demo_api_rest.exception.EntityNotFoundException;
import com.example.demo_api_rest.repository.ClientRepository;
import com.example.demo_api_rest.repository.projection.ClientProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public Cliente save(Cliente cliente) {
        try {
            return clientRepository.save(cliente);

        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException("CPF já cadastrado");
        }
    }

    @Transactional(readOnly = true)
    public Cliente findById (Long id) {
        return clientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cliente não encontrado.")
        );
    }

    @Transactional(readOnly = true)
    public Page<ClientProjection> findAll (Pageable pageable) {
        return clientRepository.findAllPage(pageable);
    }

    @Transactional(readOnly = true)
    public Cliente findByUserId (Long id) {
        return clientRepository.findByUsuarioId(id);
    }

    @Transactional(readOnly = true)
    public Cliente findByCPF (String cpf) {
        return clientRepository.findByCpf(cpf).orElseThrow(
                () -> new EntityNotFoundException("Cliente não encontrado.")
        );
    }
}
