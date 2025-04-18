package com.vr.miniautorizador.domain.exception;

public class CartaoJaExisteException extends RuntimeException{
    public CartaoJaExisteException() {
        super("Cartão já existe");
    }
}
