package com.vr.miniautorizador;

import com.vr.miniautorizador.application.CartaoServiceImpl;
import com.vr.miniautorizador.domain.Cartao;
import com.vr.miniautorizador.domain.exception.CartaoJaExisteException;
import com.vr.miniautorizador.domain.exception.CartaoNaoEncontradoException;
import com.vr.miniautorizador.domain.repository.CartaoRepository;
import com.vr.miniautorizador.infrastructure.persistence.CartaoEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartaoServiceImplTest {

    @Mock
    private CartaoRepository cartaoRepository;

    @InjectMocks
    private CartaoServiceImpl cartaoService;

    @Test
    void criarCartao_deveRetornarCartao_quandoNovo() {
        // Arrange
        Cartao cartao = new Cartao("6549873025634501", "1234");
        CartaoEntity entity = new CartaoEntity("6549873025634501", "1234", new BigDecimal("500.00"));

        when(cartaoRepository.findByNumero(any())).thenReturn(Optional.empty());
        when(cartaoRepository.save(any())).thenReturn(entity);

        // Act
        Cartao result = cartaoService.criarCartao(cartao);

        // Assert
        assertNotNull(result);
        assertEquals("6549873025634501", result.getNumero());
        verify(cartaoRepository, times(1)).save(any());
    }

    @Test
    void criarCartao_deveLancarExcecao_quandoCartaoExistir() {
        // Arrange
        Cartao cartao = new Cartao("6549873025634501", "1234");
        CartaoEntity existingEntity = new CartaoEntity("6549873025634501", "1234", new BigDecimal("500.00"));

        when(cartaoRepository.findByNumero(any())).thenReturn(Optional.of(existingEntity));

        // Act & Assert
        assertThrows(CartaoJaExisteException.class, () -> cartaoService.criarCartao(cartao));
    }

    @Test
    void obterSaldo_deveRetornarSaldo_quandoCartaoExistir() {
        // Arrange
        String numeroCartao = "6549873025634501";
        CartaoEntity entity = new CartaoEntity(numeroCartao, "1234", new BigDecimal("500.00"));

        when(cartaoRepository.findByNumero(numeroCartao)).thenReturn(Optional.of(entity));

        // Act
        BigDecimal saldo = cartaoService.obterSaldo(numeroCartao);

        // Assert
        assertEquals(new BigDecimal("500.00"), saldo);
    }

    @Test
    void obterSaldo_deveLancarExcecao_quandoCartaoNaoExistir() {
        // Arrange
        String numeroCartao = "6549873025634501";
        when(cartaoRepository.findByNumero(numeroCartao)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CartaoNaoEncontradoException.class, () -> cartaoService.obterSaldo(numeroCartao));
    }
}
