package com.vr.miniautorizador.domain.repository;

import com.vr.miniautorizador.domain.Cartao;
import com.vr.miniautorizador.infrastructure.persistence.CartaoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoRepository extends JpaRepository<CartaoEntity, Long> {

    Optional<CartaoEntity> findByNumero(String numero);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CartaoEntity c WHERE c.numero = :numero")
    Optional<CartaoEntity> findByNumeroWithLock(@Param("numero") String numero);
}
