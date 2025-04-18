package com.vr.miniautorizador.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Cartao {
    private String numero;
    private String senha;
    private BigDecimal saldo;

    public Cartao(String numero, String senha) {
        this.numero = numero;
        this.senha = senha;
        this.saldo = new BigDecimal("500.00");
    }

    public boolean senhaValida(String senha) {
        return this.senha.equals(senha);
    }

    public boolean temSaldoSuficiente(BigDecimal valor) {
        return saldo.compareTo(valor) >= 0;
    }

    public void debitar(BigDecimal valor) {
        saldo = saldo.subtract(valor);
    }
}
