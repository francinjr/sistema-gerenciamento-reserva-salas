package com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.repositories;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Long> {
    Page<Setor> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    @Query("SELECT s FROM Setor s LEFT JOIN FETCH s.salas WHERE s.status = 'ABERTO'")
    List<Setor> findAllAbertosWithSalas();

    @Query("SELECT s FROM Setor s LEFT JOIN FETCH s.salas WHERE s.status = 'ABERTO' AND lower(s.nome) LIKE lower(concat('%', :nome, '%'))")
    List<Setor> findAllAbertosWithSalasByNomeContainingIgnoreCase(@Param("nome") String nome);
}
