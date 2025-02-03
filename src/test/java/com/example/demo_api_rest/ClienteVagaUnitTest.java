package com.example.demo_api_rest;

import com.example.demo_api_rest.entity.ClienteVaga;
import com.example.demo_api_rest.entity.Vaga;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClienteVagaUnitTest {

    @Test
    void testEquals() {
        ClienteVaga clienteVaga1 = new ClienteVaga();
        clienteVaga1.setId(1L);
        clienteVaga1.setRecibo("abcd");


        ClienteVaga clienteVaga2 = new ClienteVaga();
        clienteVaga2.setId(1L);
        clienteVaga2.setRecibo("abcd");


        assertThat(clienteVaga1).isEqualTo(clienteVaga2);

        clienteVaga2.setId(2L);

        assertThat(clienteVaga1).isNotEqualTo(clienteVaga2);

    }
    @Test
    void testHashCode() {
        Vaga clienteVaga1 = new Vaga();
        clienteVaga1.setId(1L);

        Vaga clienteVaga2 = new Vaga();
        clienteVaga2.setId(1L);

        Vaga vaga3 = new Vaga();
        vaga3.setId(2L);
        assertThat(clienteVaga1.hashCode()).isEqualTo(clienteVaga2.hashCode());
        assertThat(clienteVaga1.hashCode()).isNotEqualTo(vaga3.hashCode());


    }
}
