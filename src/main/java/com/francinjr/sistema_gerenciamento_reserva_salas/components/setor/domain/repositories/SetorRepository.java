package com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.repositories;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Long> {

}
