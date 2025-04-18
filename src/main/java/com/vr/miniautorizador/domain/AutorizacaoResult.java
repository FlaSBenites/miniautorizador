package com.vr.miniautorizador.domain;

public enum AutorizacaoResult {
    APROVADA("OK"),
    SALDO_INSUFICIENTE("SALDO_INSUFICIENTE"),
    SENHA_INVALIDA("SENHA_INVALIDA"),
    CARTAO_INEXISTENTE("CARTAO_INEXISTENTE");

    private final String mensagem;

    AutorizacaoResult(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
