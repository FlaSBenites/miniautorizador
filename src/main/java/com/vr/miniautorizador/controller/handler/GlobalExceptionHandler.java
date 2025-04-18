package com.vr.miniautorizador.controller.handler;

import com.vr.miniautorizador.domain.exception.CartaoJaExisteException;
import com.vr.miniautorizador.domain.exception.CartaoNaoEncontradoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CartaoNaoEncontradoException.class)
    public ResponseEntity<Void> handleCartaoNaoEncontrado() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(CartaoJaExisteException.class)
    public ResponseEntity<Void> handleCartaoJaExiste() {
        return ResponseEntity.unprocessableEntity().build();
    }
}