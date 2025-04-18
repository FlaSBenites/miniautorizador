package com.vr.miniautorizador.controller.dto;

import com.vr.miniautorizador.domain.Cartao;

public record CartaoResponseDto(
        String numeroCartao,
        String senha
) {
    public static CartaoResponseDto fromDomain(Cartao cartao) {
        return new CartaoResponseDto(cartao.getNumero(), cartao.getSenha());
    }

    public static CartaoResponseDto fromRequest(CartaoRequestDto request) {
        return new CartaoResponseDto(request.numeroCartao(), request.senha());
    }
}