package com.vr.miniautorizador;

import com.vr.miniautorizador.application.TransacaoServiceImpl;
import com.vr.miniautorizador.application.TransacaoUseCase;
import com.vr.miniautorizador.config.SecurityConfig;
import com.vr.miniautorizador.controller.TransacaoController;
import com.vr.miniautorizador.domain.AutorizacaoResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransacaoController.class)
@AutoConfigureMockMvc
@Import({SecurityConfig.class, TransacaoServiceImpl.class})
@ContextConfiguration(classes = TransacaoController.class)
class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransacaoUseCase transacaoUseCase;

    @Test
    void autorizarTransacao_deveRetornar201_quandoAprovada() throws Exception {
        // Arrange
        when(transacaoUseCase.autorizarTransacao(any())).thenReturn(AutorizacaoResult.APROVADA);

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numeroCartao\":\"6549873025634501\",\"senhaCartao\":\"1234\",\"valor\":10.00}")
                        .with(httpBasic("username", "password")))
                .andExpect(status().isCreated())
                .andExpect(content().string("OK"));
    }

    @Test
    void autorizarTransacao_deveRetornar422_quandoSaldoInsuficiente() throws Exception {
        // Arrange
        when(transacaoUseCase.autorizarTransacao(any()))
                .thenReturn(AutorizacaoResult.SALDO_INSUFICIENTE);

        // Act & Assert
        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numeroCartao\":\"6549873025634501\",\"senhaCartao\":\"1234\",\"valor\":600.00}")
                        .with(httpBasic("username", "password")))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SALDO_INSUFICIENTE"));
    }
}
