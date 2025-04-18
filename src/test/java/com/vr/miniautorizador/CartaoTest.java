package com.vr.miniautorizador;

import com.vr.miniautorizador.domain.Cartao;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CartaoTest {

    @Test
    void debitar_deveSubtrairValorDoSaldo() {
        Cartao cartao = new Cartao("1234567890123456", "1234");
        BigDecimal valorInicial = cartao.getSaldo();
        BigDecimal valorDebito = new BigDecimal("100.00");

        cartao.debitar(valorDebito);

        assertEquals(valorInicial.subtract(valorDebito), cartao.getSaldo());
    }

    @Test
    void senhaValida_deveRetornarTrue_quandoSenhaCorreta() {
        Cartao cartao = new Cartao("1234567890123456", "1234");
        assertTrue(cartao.senhaValida("1234"));
    }

    @Test
    void senhaValida_deveRetornarFalse_quandoSenhaIncorreta() {
        Cartao cartao = new Cartao("1234567890123456", "1234");
        assertFalse(cartao.senhaValida("0000"));
    }

    @Test
    void temSaldoSuficiente_deveRetornarTrue_quandoSaldoMaiorOuIgual() {
        Cartao cartao = new Cartao("1234567890123456", "1234");
        assertTrue(cartao.temSaldoSuficiente(new BigDecimal("500.00")));
        assertTrue(cartao.temSaldoSuficiente(new BigDecimal("499.99")));
    }

    @Test
    void temSaldoSuficiente_deveRetornarFalse_quandoSaldoMenor() {
        Cartao cartao = new Cartao("1234567890123456", "1234");
        assertFalse(cartao.temSaldoSuficiente(new BigDecimal("500.01")));
    }
}
