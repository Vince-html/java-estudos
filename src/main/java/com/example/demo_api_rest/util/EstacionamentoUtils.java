package com.example.demo_api_rest.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EstacionamentoUtils {

    private static final double PRIMEIROS_15_MINUTOS = 5.00;
    private static final double PRIMEIROS_60_MINUTOS = 9.25;
    private static final double ADICIONAL_15_MINUTOS = 1.75;
    private static final double DESCONTO_PERCENTUAL = 0.30;

    public static String gerarRecibo () {
        LocalDateTime date = LocalDateTime.now();
        String recibo = date.toString().substring(0,19);
        return recibo.replace("-", "")
                .replace(":", "")
                .replace("T", "-");
    }

    public static BigDecimal calcularCusto(LocalDateTime entrada, LocalDateTime saida) {
        long minutos = entrada.until(saida, ChronoUnit.MINUTES);
        double total;

        if (minutos <= 15) {
            total = PRIMEIROS_15_MINUTOS;
        } else if (minutos <= 60) {
            total = PRIMEIROS_60_MINUTOS;
        } else {
            total = PRIMEIROS_60_MINUTOS;
            long minutosExtras = minutos - 60;
            long blocosDe15Minutos = (minutosExtras + 14) / 15; // Arredondar para cima
            total += blocosDe15Minutos * ADICIONAL_15_MINUTOS;
        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calcularDesconto(BigDecimal custo, long numeroDeVezes) {
        BigDecimal desconto = ((numeroDeVezes > 0) && (numeroDeVezes % 10 == 0))
                ? custo.multiply(new BigDecimal(DESCONTO_PERCENTUAL))
                : new BigDecimal(0);
        return desconto.setScale(2, RoundingMode.HALF_EVEN);
    }
}
