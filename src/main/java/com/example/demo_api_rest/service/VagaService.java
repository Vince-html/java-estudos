package com.example.demo_api_rest.service;


import com.example.demo_api_rest.entity.Vaga;
import com.example.demo_api_rest.exception.CodigoUniqueViolationException;
import com.example.demo_api_rest.exception.EntityNotFoundException;
import com.example.demo_api_rest.repository.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@RequiredArgsConstructor
@Service
public class VagaService {

    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga save(Vaga vaga) {
        try {
            return vagaRepository.save(vaga);

        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new CodigoUniqueViolationException("Vaga já cadastrada");
        }
    }

    @Transactional(readOnly = true)
    public Vaga findByCodigo (String codigo) {
        return vagaRepository.findByCodigo(codigo).orElseThrow(
                () -> new EntityNotFoundException("Vaga não encontrado.")
        );
    }

}
