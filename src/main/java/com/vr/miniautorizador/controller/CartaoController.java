package com.vr.miniautorizador.controller;

import com.vr.miniautorizador.application.CartaoUseCase;
import com.vr.miniautorizador.controller.dto.CartaoRequestDto;
import com.vr.miniautorizador.controller.dto.CartaoResponseDto;
import com.vr.miniautorizador.domain.exception.CartaoJaExisteException;
import com.vr.miniautorizador.domain.exception.CartaoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CartaoController {

    private final CartaoUseCase cartaoUseCase;

    @PostMapping
    public ResponseEntity<CartaoResponseDto> criarCartao(@RequestBody CartaoRequestDto cartaoRequest) {
        try {
            var cartao = cartaoUseCase.criarCartao(cartaoRequest.toDomain());
            return ResponseEntity.status(HttpStatus.CREATED).body(CartaoResponseDto.fromDomain(cartao));
        } catch (CartaoJaExisteException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.unprocessableEntity().body(CartaoResponseDto.fromRequest(cartaoRequest));
        }
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> obterSaldo(@PathVariable String numeroCartao) {
        try {
            return ResponseEntity.ok(cartaoUseCase.obterSaldo(numeroCartao));
        } catch (CartaoNaoEncontradoException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}