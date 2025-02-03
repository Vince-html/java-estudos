package com.example.demo_api_rest;

import com.example.demo_api_rest.entity.Cliente;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ClientUnitTest {

    @Test
    void testEquals() {
        Cliente client1 = new Cliente();
        client1.setId(1L);
        client1.setNome("Cliente 1");
        client1.setCpf("12345678901");

        Cliente client2 = new Cliente();
        client2.setId(1L);
        client2.setNome("Cliente 2");
        client2.setCpf("98765432100");
        assertThat(client1).isEqualTo(client2);

        client2.setId(2L);
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void testHashCode() {
        Cliente client1 = new Cliente();
        client1.setId(1L);

        Cliente client2 = new Cliente();
        client2.setId(1L);

        Cliente client3 = new Cliente();
        client3.setId(2L);
        assertThat(client1.hashCode()).isEqualTo(client2.hashCode());
        assertThat(client1.hashCode()).isNotEqualTo(client3.hashCode());
    }
}