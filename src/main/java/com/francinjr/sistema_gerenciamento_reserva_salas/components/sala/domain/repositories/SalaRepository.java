package com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.repositories;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.entities.Sala;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    Page<Sala> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    List<Sala> findBySetor(Setor setor);
}
