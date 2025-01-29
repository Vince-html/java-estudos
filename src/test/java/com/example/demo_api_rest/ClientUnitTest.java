package com.example.demo_api_rest;

import com.example.demo_api_rest.entity.Client;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ClientUnitTest {

    @Test
    void testEquals() {
        Client client1 = new Client();
        client1.setId(1L);
        client1.setNome("Cliente 1");
        client1.setCpf("12345678901");

        Client client2 = new Client();
        client2.setId(1L);
        client2.setNome("Cliente 2");
        client2.setCpf("98765432100");
        assertThat(client1).isEqualTo(client2);

        client2.setId(2L);
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void testHashCode() {
        Client client1 = new Client();
        client1.setId(1L);

        Client client2 = new Client();
        client2.setId(1L);

        Client client3 = new Client();
        client3.setId(2L);
        assertThat(client1.hashCode()).isEqualTo(client2.hashCode());
        assertThat(client1.hashCode()).isNotEqualTo(client3.hashCode());
    }
}