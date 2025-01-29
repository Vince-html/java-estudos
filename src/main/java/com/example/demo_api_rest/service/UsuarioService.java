package com.example.demo_api_rest.service;

import com.example.demo_api_rest.entity.Usuario;
import com.example.demo_api_rest.exception.EntityNotFoundException;
import com.example.demo_api_rest.exception.PasswordInvalidException;
import com.example.demo_api_rest.exception.UserNameUniqueViolationException;
import com.example.demo_api_rest.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario save(Usuario usuario) {
        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new UserNameUniqueViolationException("Usuário já cadastrado");
        }

    }

    @Transactional(readOnly = true)
    public Usuario findById (Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Usuário não encontrado.")
        );
    }

    @Transactional
    public Usuario changePass (Long id, String password, String newPassword, String newPasswordConfirm) {
        if(!newPassword.equals(newPasswordConfirm)){
            throw  new PasswordInvalidException("Nova senha não confere com a confirmação!");
        }
        Usuario user = findById(id);
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw  new PasswordInvalidException("Senha inválida!");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAll () {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Usuario> findByName(String name) {
        return usuarioRepository.findByUsernameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public Usuario findByUniqueEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Usuário não encontrado.")
        );
    }
    @Transactional(readOnly = true)
    public List<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmailContainingIgnoreCase(email);
    }

    @Transactional(readOnly = true)
    public Usuario.Role findRoleByEmail(String email) {
        return usuarioRepository.findByRoleByEmail(email);
    }
}
