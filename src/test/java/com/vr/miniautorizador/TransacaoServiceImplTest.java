package com.vr.miniautorizador;

import com.vr.miniautorizador.application.TransacaoServiceImpl;
import com.vr.miniautorizador.domain.AutorizacaoResult;
import com.vr.miniautorizador.domain.Transacao;
import com.vr.miniautorizador.domain.repository.CartaoRepository;
import com.vr.miniautorizador.infrastructure.persistence.CartaoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceImplTest {

    @Mock
    private CartaoRepository cartaoRepository;

    @InjectMocks
    private TransacaoServiceImpl transacaoService;

    @Test
    void autorizarTransacao_deveRetornarAprovada_quandoTudoValido() {
        // Arrange
        Transacao transacao = new Transacao("6549873025634501", "1234", new BigDecimal("100.00"));
        CartaoEntity entity = new CartaoEntity("6549873025634501", "1234", new BigDecimal("500.00"));

        when(cartaoRepository.findByNumeroWithLock("6549873025634501")).thenReturn(Optional.of(entity));
        when(cartaoRepository.save(any())).thenReturn(entity);

        // Act
        AutorizacaoResult result = transacaoService.autorizarTransacao(transacao);

        // Assert
        assertEquals(AutorizacaoResult.APROVADA, result);
        verify(cartaoRepository).findByNumeroWithLock("6549873025634501");
        verify(cartaoRepository).save(entity);
    }

    @Test
    void autorizarTransacao_deveRetornarCartaoInexistente_quandoCartaoNaoEncontrado() {
        // Arrange
        Transacao transacao = new Transacao("6549873025634501", "1234", new BigDecimal("100.00"));
        when(cartaoRepository.findByNumeroWithLock("6549873025634501")).thenReturn(Optional.empty());

        // Act
        AutorizacaoResult result = transacaoService.autorizarTransacao(transacao);

        // Assert
        assertEquals(AutorizacaoResult.CARTAO_INEXISTENTE, result);
        verify(cartaoRepository).findByNumeroWithLock("6549873025634501");
        verify(cartaoRepository, never()).save(any());
    }

    @Test
    void autorizarTransacao_deveRetornarSenhaInvalida_quandoSenhaIncorreta() {
        // Arrange
        Transacao transacao = new Transacao("6549873025634501", "0000", new BigDecimal("100.00"));
        CartaoEntity entity = new CartaoEntity("6549873025634501", "1234", new BigDecimal("500.00"));

        when(cartaoRepository.findByNumeroWithLock("6549873025634501")).thenReturn(Optional.of(entity));

        // Act
        AutorizacaoResult result = transacaoService.autorizarTransacao(transacao);

        // Assert
        assertEquals(AutorizacaoResult.SENHA_INVALIDA, result);
        verify(cartaoRepository, never()).save(any());
    }

    @Test
    void autorizarTransacao_deveRetornarSaldoInsuficiente_quandoSaldoMenorQueValor() {
        // Arrange
        Transacao transacao = new Transacao("6549873025634501", "1234", new BigDecimal("600.00"));
        CartaoEntity entity = new CartaoEntity("6549873025634501", "1234", new BigDecimal("500.00"));

        when(cartaoRepository.findByNumeroWithLock("6549873025634501")).thenReturn(Optional.of(entity));

        // Act
        AutorizacaoResult result = transacaoService.autorizarTransacao(transacao);

        // Assert
        assertEquals(AutorizacaoResult.SALDO_INSUFICIENTE, result);
        verify(cartaoRepository, never()).save(any());
    }

    @Test
    void processarTransacao_deveDebitarSaldo_quandoAprovada() {
        // Arrange
        Transacao transacao = new Transacao("6549873025634501", "1234", new BigDecimal("100.00"));
        CartaoEntity entity = new CartaoEntity("6549873025634501", "1234", new BigDecimal("500.00"));

        // Act
       AutorizacaoResult result = transacaoService.autorizarTransacao(transacao);

        // Assert
        assertEquals(AutorizacaoResult.APROVADA, result);
        assertEquals(new BigDecimal("400.00"), entity.getSaldo());
    }
}