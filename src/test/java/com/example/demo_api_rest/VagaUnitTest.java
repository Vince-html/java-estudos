package com.example.demo_api_rest;

import com.example.demo_api_rest.entity.Vaga;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VagaUnitTest {

    @Test
    void testEquals() {
        Vaga vaga1 = new Vaga();
        vaga1.setId(1L);
        vaga1.setCodigo("abcd");


        Vaga vaga2 = new Vaga();
        vaga2.setId(1L);
        vaga2.setCodigo("abcd");


        assertThat(vaga1).isEqualTo(vaga2);

        vaga2.setId(2L);

        assertThat(vaga1).isNotEqualTo(vaga2);

    }
    @Test
    void testHashCode() {
        Vaga vaga1 = new Vaga();
        vaga1.setId(1L);

        Vaga vaga2 = new Vaga();
        vaga2.setId(1L);

        Vaga vaga3 = new Vaga();
        vaga3.setId(2L);
        assertThat(vaga1.hashCode()).isEqualTo(vaga2.hashCode());
        assertThat(vaga1.hashCode()).isNotEqualTo(vaga3.hashCode());


    }
}
