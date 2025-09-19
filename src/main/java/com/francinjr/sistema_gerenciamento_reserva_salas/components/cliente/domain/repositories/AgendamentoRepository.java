package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.repositories;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Agendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.StatusAgendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    /**
     * Busca todas as solicitações com um status específico, de forma paginada.
     * Usado para a tela de listagem da recepcionista.
     */
    Page<Agendamento> findByStatus(StatusAgendamento status, Pageable pageable);

    /**
     * Busca solicitações conflitantes que precisam ser canceladas automaticamente.
     * Encontra todos os agendamentos SOLICITADOS para uma dada sala, que se sobrepõem
     * a um determinado intervalo de tempo, e que não sejam o próprio agendamento que está sendo confirmado.
     */
    @Query("SELECT a FROM Agendamento a WHERE a.id != :agendamentoId " +
            "AND a.sala.id = :salaId " +
            "AND a.status = 'SOLICITADO' " +
            "AND a.dataHoraInicio < :dataHoraFim " +
            "AND a.dataHoraFim > :dataHoraInicio")
    List<Agendamento> findConflictingRequests(
            @Param("agendamentoId") Long agendamentoId,
            @Param("salaId") Long salaId,
            @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
            @Param("dataHoraFim") LocalDateTime dataHoraFim
    );

    Page<Agendamento> findBySalaSetorAndStatus(Setor setor, StatusAgendamento status, Pageable pageable);

    Page<Agendamento> findBySalaSetorAndStatusIn(Setor setor, List<StatusAgendamento> statuses, Pageable pageable);
}
