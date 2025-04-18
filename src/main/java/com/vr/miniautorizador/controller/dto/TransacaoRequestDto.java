package com.vr.miniautorizador.controller.dto;

import com.vr.miniautorizador.domain.Transacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransacaoRequestDto(
        @NotBlank
        @Pattern(regexp = "\\d{16}", message = "Número do cartão deve ter 16 dígitos")
        String numeroCartao,

        @NotBlank
        @Size(min = 4, max = 4, message = "Senha deve ter 4 dígitos")
        String senhaCartao,

        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal valor
) {
    public Transacao toDomain() {
        return new Transacao(numeroCartao, senhaCartao, valor);
    }
}