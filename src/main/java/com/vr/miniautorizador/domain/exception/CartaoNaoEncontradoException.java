package com.vr.miniautorizador.domain.exception;

public class CartaoNaoEncontradoException extends RuntimeException {
    public CartaoNaoEncontradoException() {
        super("Cartão não encontrado");
    }
}
