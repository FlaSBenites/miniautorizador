package com.vr.miniautorizador;

import com.vr.miniautorizador.application.TransacaoUseCase;
import com.vr.miniautorizador.domain.AutorizacaoResult;
import com.vr.miniautorizador.domain.Transacao;
import com.vr.miniautorizador.domain.repository.CartaoRepository;
import com.vr.miniautorizador.infrastructure.persistence.CartaoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransacaoConcurrenceTest {

    @Autowired
    private TransacaoUseCase transacaoService;

    @Autowired
    private CartaoRepository cartaoRepository;

    @BeforeEach
    void setup() {
        // Limpar a tabela de cartões e inserir um cartão inicial
        cartaoRepository.deleteAll();
        CartaoEntity cartao = new CartaoEntity("6549873025634501", "1234", new BigDecimal("500.00"));
        cartaoRepository.save(cartao);
    }

    @Test
    void testConcorrenciaEmTransacoes() throws InterruptedException {
        // Criar duas transações concorrentes
        Transacao transacao1 = new Transacao("6549873025634501", "1234", new BigDecimal("400.00"));
        Transacao transacao2 = new Transacao("6549873025634501", "1234", new BigDecimal("200.00"));

        // Criar uma pool de threads para simular as transações concorrentes
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Future<AutorizacaoResult>> resultados = new ArrayList<>();

        // Enviar as transações para execução concorrente
        resultados.add(executor.submit(() -> transacaoService.autorizarTransacao(transacao1)));
        resultados.add(executor.submit(() -> transacaoService.autorizarTransacao(transacao2)));

        // Fechar o executor e esperar as transações terminarem
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Contar quantas transações foram aprovadas
        long aprovadas = resultados.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(result -> result == AutorizacaoResult.APROVADA)
                .count();

        // Assegurar que apenas uma transação foi aprovada
        assertEquals(1, aprovadas, "Apenas uma transação deve ser aprovada");

        // Validar o saldo do cartão após as transações
        BigDecimal saldoFinal = cartaoRepository.findByNumero("6549873025634501")
                .map(CartaoEntity::getSaldo)
                .orElseThrow(() -> new AssertionError("Cartão não encontrado"));

        // O saldo final deve refletir o sucesso de apenas uma transação
        assertTrue(
                saldoFinal.equals(new BigDecimal("100.00")) || saldoFinal.equals(new BigDecimal("300.00")),
                "Saldo final deve refletir apenas uma transação"
        );
    }
}



