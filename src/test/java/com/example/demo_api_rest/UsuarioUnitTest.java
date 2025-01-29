package com.example.demo_api_rest;

import com.example.demo_api_rest.entity.Usuario;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UsuarioUnitTest {

    @Test
    void testEquals() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setEmail("email@email.com");

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);
        usuario2.setEmail("email@email.com");

        assertThat(usuario1).isEqualTo(usuario2);

        usuario2.setId(2L);

        assertThat(usuario1).isNotEqualTo(usuario2);

    }
    @Test
    void testHashCode() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);

        Usuario usuario3 = new Usuario();
        usuario3.setId(2L);
        assertThat(usuario1.hashCode()).isEqualTo(usuario2.hashCode());
        assertThat(usuario1.hashCode()).isNotEqualTo(usuario3.hashCode());


    }
}
