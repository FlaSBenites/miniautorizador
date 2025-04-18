package com.vr.miniautorizador.application;

import com.vr.miniautorizador.domain.AutorizacaoResult;
import com.vr.miniautorizador.domain.Cartao;
import com.vr.miniautorizador.domain.Transacao;
import com.vr.miniautorizador.domain.repository.CartaoRepository;
import com.vr.miniautorizador.infrastructure.persistence.CartaoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransacaoServiceImpl implements TransacaoUseCase {

    private final CartaoRepository cartaoRepository;

    @Override
    @Transactional
    public AutorizacaoResult autorizarTransacao(Transacao transacao) {
        return cartaoRepository.findByNumero(transacao.getNumeroCartao())
                .map(cartaoEntity -> processarTransacao(cartaoEntity, transacao))
                .orElse(AutorizacaoResult.CARTAO_INEXISTENTE);
    }

    private AutorizacaoResult processarTransacao(CartaoEntity cartaoEntity, Transacao transacao) {

        // Converte para domínio para aplicar as regras de negócio
        Cartao cartao = cartaoEntity.toDomain();

        if (!cartao.senhaValida(transacao.getSenhaCartao())) {
            return AutorizacaoResult.SENHA_INVALIDA;
        }

        if (!cartao.temSaldoSuficiente(transacao.getValor())) {
            return AutorizacaoResult.SALDO_INSUFICIENTE;
        }

        // Aplica o débito no objeto de domínio
        cartao.debitar(transacao.getValor());
        // Atualiza a entidade com os novos valores
        cartaoEntity.setSaldo(cartao.getSaldo());
        // Persiste a entidade atualizada
        cartaoRepository.save(cartaoEntity);

        return AutorizacaoResult.APROVADA;
    }
}
