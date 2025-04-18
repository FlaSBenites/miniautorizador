package com.vr.miniautorizador.controller.dto;

import com.vr.miniautorizador.domain.Cartao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CartaoRequestDto(
        @NotBlank
        @Pattern(regexp = "\\d{16}", message = "Número do cartão deve ter 16 dígitos")
        String numeroCartao,

        @NotBlank
        @Size(min = 4, max = 4, message = "Senha deve ter 4 dígitos")
        String senha
) {
    public Cartao toDomain() {
        return new Cartao(numeroCartao, senha);
    }
}