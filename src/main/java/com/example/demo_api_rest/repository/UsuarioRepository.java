package com.example.demo_api_rest.repository;

import com.example.demo_api_rest.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByUsernameContainingIgnoreCase(String username);
    List<Usuario> findByEmailContainingIgnoreCase(String email);
    @Query("select u.role from Usuario u where u.email like :email")
    Usuario.Role findByRoleByEmail(String email);
}