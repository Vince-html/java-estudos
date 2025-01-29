package com.example.demo_api_rest.repository;

import com.example.demo_api_rest.entity.Client;
import com.example.demo_api_rest.repository.projection.ClientProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("select c from Client c")
    Page<ClientProjection> findAllPage(Pageable pageable);

    Client findByUsuarioId(Long id);
}
