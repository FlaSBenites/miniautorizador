package com.vr.miniautorizador.application;

import com.vr.miniautorizador.domain.Cartao;
import com.vr.miniautorizador.domain.exception.CartaoJaExisteException;
import com.vr.miniautorizador.domain.exception.CartaoNaoEncontradoException;
import com.vr.miniautorizador.domain.repository.CartaoRepository;
import com.vr.miniautorizador.infrastructure.persistence.CartaoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartaoServiceImpl implements CartaoUseCase {

    private final CartaoRepository cartaoRepository;

    @Override
    @Transactional
    public Cartao criarCartao(Cartao cartao) {
        // Verifica se cartão já existe
        if (cartaoRepository.findByNumero(cartao.getNumero()).isPresent()) {
            throw new CartaoJaExisteException();
        }

        // Converte e salva
        CartaoEntity entity = new CartaoEntity(
                cartao.getNumero(),
                cartao.getSenha(),
                cartao.getSaldo()
        );

        CartaoEntity savedEntity = cartaoRepository.save(entity);

        // Retorna o domínio convertido
        return new Cartao(
                savedEntity.getNumero(),
                savedEntity.getSenha(),
                savedEntity.getSaldo()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal obterSaldo(String numeroCartao) {
        return cartaoRepository.findByNumero(numeroCartao)
                .map(CartaoEntity::getSaldo)
                .orElseThrow(CartaoNaoEncontradoException::new);
    }
}