package com.vr.miniautorizador.domain.repository;

import com.vr.miniautorizador.domain.Cartao;
import com.vr.miniautorizador.infrastructure.persistence.CartaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoRepository extends JpaRepository<CartaoEntity, Long> {
    Optional<CartaoEntity> findByNumero(String numero);
}
