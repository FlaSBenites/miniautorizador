package com.vr.miniautorizador;

import com.vr.miniautorizador.application.CartaoServiceImpl;
import com.vr.miniautorizador.application.CartaoUseCase;
import com.vr.miniautorizador.config.SecurityConfig;
import com.vr.miniautorizador.controller.CartaoController;
import com.vr.miniautorizador.controller.dto.CartaoResponseDto;
import com.vr.miniautorizador.domain.Cartao;
import com.vr.miniautorizador.domain.exception.CartaoJaExisteException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartaoController.class)
@AutoConfigureMockMvc
@Import({SecurityConfig.class, CartaoServiceImpl.class})
@ContextConfiguration(classes = {CartaoController.class})
class CartaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartaoUseCase cartaoUseCase;

    @Test
    void criarCartao_deveRetornar201_quandoSucesso() throws Exception {
        // Arrange
        CartaoResponseDto responseDto = new CartaoResponseDto("6549873025634501", "1234");
        when(cartaoUseCase.criarCartao(any())).thenReturn(new Cartao("6549873025634501", "1234", new BigDecimal("500.00")));

        // Act & Assert
        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numeroCartao\":\"6549873025634501\",\"senha\":\"1234\"}")
                        .with(httpBasic("username", "password")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCartao").value("6549873025634501"))
                .andExpect(jsonPath("$.senha").value("1234"));
    }

    @Test
    void criarCartao_deveRetornar422_quandoCartaoExistir() throws Exception {
        // Arrange
        when(cartaoUseCase.criarCartao(any()))
                .thenThrow(new CartaoJaExisteException());

        // Act & Assert
        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numeroCartao\":\"6549873025634501\",\"senha\":\"1234\"}")
                        .with(httpBasic("username", "password")))
                .andExpect(status().isUnprocessableEntity());
    }
}
