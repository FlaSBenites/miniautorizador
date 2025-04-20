package com.vr.miniautorizador.application;

import com.vr.miniautorizador.domain.AutorizacaoResult;
import com.vr.miniautorizador.domain.Cartao;
import com.vr.miniautorizador.domain.Transacao;
import com.vr.miniautorizador.domain.repository.CartaoRepository;
import com.vr.miniautorizador.infrastructure.persistence.CartaoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransacaoServiceImpl implements TransacaoUseCase {

    private final CartaoRepository cartaoRepository;

    @Retryable(
            value = { ObjectOptimisticLockingFailureException.class, PessimisticLockingFailureException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )

    @Override
    @Transactional
    public AutorizacaoResult autorizarTransacao(Transacao transacao) {
        CartaoEntity cartaoEntity = cartaoRepository.findByNumeroWithLock(transacao.getNumeroCartao()).orElse(null);
        if(cartaoEntity == null){
            return  AutorizacaoResult.CARTAO_INEXISTENTE;
        }

        Cartao cartao = cartaoEntity.toDomain();
        AutorizacaoResult result = processarTransacao(cartaoEntity, transacao);

        if(result == AutorizacaoResult.APROVADA){
            cartao.debitar(transacao.getValor());
            cartaoEntity.setSaldo(cartao.getSaldo());
        }

        return result;
    }

    public AutorizacaoResult processarTransacao(CartaoEntity cartaoEntity, Transacao transacao) {

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
