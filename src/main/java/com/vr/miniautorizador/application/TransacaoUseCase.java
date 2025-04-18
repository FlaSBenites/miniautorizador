package com.vr.miniautorizador.application;

import com.vr.miniautorizador.domain.AutorizacaoResult;
import com.vr.miniautorizador.domain.Transacao;

public interface TransacaoUseCase {
    AutorizacaoResult autorizarTransacao(Transacao transacao);
}

