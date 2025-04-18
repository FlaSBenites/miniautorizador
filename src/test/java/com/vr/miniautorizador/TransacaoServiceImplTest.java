package com.vr.miniautorizador;

import com.vr.miniautorizador.application.TransacaoServiceImpl;
import com.vr.miniautorizador.domain.AutorizacaoResult;
import com.vr.miniautorizador.domain.Transacao;
import com.vr.miniautorizador.domain.repository.CartaoRepository;
import com.vr.miniautorizador.infrastructure.persistence.CartaoEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Transacao transacao = new Transacao("6549873025634501", "1234", new BigDecimal("100.00"));
        CartaoEntity entity = new CartaoEntity("6549873025634501", "1234", new BigDecimal("500.00"));

        when(cartaoRepository.findByNumero("6549873025634501")).thenReturn(Optional.of(entity));
        when(cartaoRepository.save(any())).thenReturn(entity);

        AutorizacaoResult result = transacaoService.autorizarTransacao(transacao);

        assertEquals(AutorizacaoResult.APROVADA, result);
        verify(cartaoRepository, times(1)).save(any());
    }

    @Test
    void autorizarTransacao_deveRetornarCartaoInexistente_quandoCartaoNaoEncontrado() {
        Transacao transacao = new Transacao("6549873025634501", "1234", new BigDecimal("100.00"));
        when(cartaoRepository.findByNumero("6549873025634501")).thenReturn(Optional.empty());

        AutorizacaoResult result = transacaoService.autorizarTransacao(transacao);

        assertEquals(AutorizacaoResult.CARTAO_INEXISTENTE, result);
    }

    @Test
    void autorizarTransacao_deveRetornarSenhaInvalida_quandoSenhaIncorreta() {
        Transacao transacao = new Transacao("6549873025634501", "0000", new BigDecimal("100.00"));
        CartaoEntity entity = new CartaoEntity("6549873025634501", "1234", new BigDecimal("500.00"));

        when(cartaoRepository.findByNumero("6549873025634501")).thenReturn(Optional.of(entity));

        AutorizacaoResult result = transacaoService.autorizarTransacao(transacao);

        assertEquals(AutorizacaoResult.SENHA_INVALIDA, result);
    }

    @Test
    void autorizarTransacao_deveRetornarSaldoInsuficiente_quandoSaldoMenorQueValor() {
        Transacao transacao = new Transacao("6549873025634501", "1234", new BigDecimal("600.00"));
        CartaoEntity entity = new CartaoEntity("6549873025634501", "1234", new BigDecimal("500.00"));

        when(cartaoRepository.findByNumero("6549873025634501")).thenReturn(Optional.of(entity));

        AutorizacaoResult result = transacaoService.autorizarTransacao(transacao);

        assertEquals(AutorizacaoResult.SALDO_INSUFICIENTE, result);
    }
}
