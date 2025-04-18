package com.vr.miniautorizador.infrastructure.persistence;

import com.vr.miniautorizador.domain.Cartao;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cartoes")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 16)
    private String numero;

    @Column(nullable = false, length = 4)
    private String senha;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;

    // Construtor para conversão
    public CartaoEntity(String numero, String senha, BigDecimal saldo) {
        this.numero = numero;
        this.senha = senha;
        this.saldo = saldo;
    }

    public static CartaoEntity fromDomain(Cartao cartao) {
        return new CartaoEntity(
                cartao.getNumero(),
                cartao.getSenha(),
                cartao.getSaldo()
        );
    }

    public Cartao toDomain() {
        return new Cartao(numero, senha, saldo);
    }
}
