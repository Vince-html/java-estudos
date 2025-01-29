package com.example.demo_api_rest.jwt;


import com.example.demo_api_rest.entity.Usuario;
import com.example.demo_api_rest.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UsuarioService usuarioService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.findByUniqueEmail(email);
        return new JwtUserDetails(usuario);
    }


    public JwtToken getTokenAuthenticated (String email) {
        Usuario.Role role = usuarioService.findRoleByEmail(email);
        return JwtUtils.createToken(email, role.name().substring("ROLE_".length()));
    }
}
