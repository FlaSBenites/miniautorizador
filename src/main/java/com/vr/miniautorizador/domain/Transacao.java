package com.vr.miniautorizador.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Transacao {
    private String numeroCartao;
    private String senhaCartao;
    private BigDecimal valor;
}
