package com.example.demo_api_rest.repository;

import com.example.demo_api_rest.entity.Cliente;
import com.example.demo_api_rest.repository.projection.ClientProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Cliente, Long> {

    @Query("select c from Cliente c")
    Page<ClientProjection> findAllPage(Pageable pageable);

    Cliente findByUsuarioId(Long id);

    Optional<Cliente> findByCpf(String cpf);
}
