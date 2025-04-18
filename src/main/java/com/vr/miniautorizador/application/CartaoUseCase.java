package com.vr.miniautorizador.application;

import com.vr.miniautorizador.domain.Cartao;
import java.math.BigDecimal;

public interface CartaoUseCase {
    Cartao criarCartao(Cartao cartao);
    BigDecimal obterSaldo(String numeroCartao);
}
