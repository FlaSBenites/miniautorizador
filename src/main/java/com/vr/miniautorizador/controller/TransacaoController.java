package com.vr.miniautorizador.controller;

import com.vr.miniautorizador.application.TransacaoUseCase;
import com.vr.miniautorizador.controller.dto.TransacaoRequestDto;
import com.vr.miniautorizador.domain.AutorizacaoResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoUseCase transacaoUseCase;

    @PostMapping
    public ResponseEntity<String> autorizarTransacao(@RequestBody TransacaoRequestDto transacaoRequest) {
        AutorizacaoResult result = transacaoUseCase.autorizarTransacao(transacaoRequest.toDomain());

        return result.equals(AutorizacaoResult.APROVADA)
                ? ResponseEntity.status(HttpStatus.CREATED).body(result.getMensagem())
                : ResponseEntity.unprocessableEntity().body(result.getMensagem());
    }
}