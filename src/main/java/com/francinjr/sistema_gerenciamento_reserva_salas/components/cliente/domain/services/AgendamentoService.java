package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.services;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.RecursoNaoEncontradoException;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Agendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Cliente;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.StatusAgendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.repositories.AgendamentoRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos.SalvarAgendamentoDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.entities.Sala;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.services.SalaService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final SalaService salaService;

    @Transactional
    public Agendamento solicitar(SalvarAgendamentoDto dto, Cliente cliente) {
        Sala sala = salaService.buscarPorId(dto.getSalaId());
        Agendamento novoAgendamento = new Agendamento(
                dto.getDataHoraInicio(), dto.getDataHoraFim(), sala, cliente,
                dto.getQuantidadePessoas());
        return agendamentoRepository.save(novoAgendamento);
    }

    @Transactional(readOnly = true)
    public Page<Agendamento> buscarSolicitacoesPendentes(Setor setor, Pageable pageable) {
        return agendamentoRepository.findBySalaSetorAndStatus(setor, StatusAgendamento.SOLICITADO, pageable);
    }


    @Transactional(readOnly = true)
    public Agendamento buscarSolicitacaoDoCliente(Long agendamentoId, Cliente cliente) {
        Agendamento agendamento = buscarAgendamentoPorId(agendamentoId);

        if (!agendamento.pertenceAoCliente(cliente)) {
            throw new SecurityException("Acesso negado: esta solicitação não pertence a você.");
        }

        return agendamento;
    }

    @Transactional
    public Agendamento confirmar(Long agendamentoId) {
        Agendamento agendamento = buscarAgendamentoPorId(agendamentoId);
        agendamento.confirmar();
        Agendamento agendamentoConfirmado = agendamentoRepository.save(agendamento);
        cancelarSolicitacoesConflitantes(agendamentoConfirmado);
        return agendamentoConfirmado;
    }

    @Transactional
    public void recusar(Long agendamentoId) {
        Agendamento agendamento = buscarAgendamentoPorId(agendamentoId);
        agendamento.recusar();
        agendamentoRepository.save(agendamento);
    }

    @Transactional
    public void cancelarSolicitacao(Long agendamentoId, Cliente cliente) {
        Agendamento agendamento = buscarSolicitacaoDoCliente(agendamentoId, cliente);
        agendamento.cancelar();
        agendamentoRepository.save(agendamento);
    }


    private void cancelarSolicitacoesConflitantes(Agendamento agendamentoConfirmado) {
        List<Agendamento> solicitacoesConflitantes = agendamentoRepository.findConflictingRequests(
                agendamentoConfirmado.getId(),
                agendamentoConfirmado.getSala().getId(),
                agendamentoConfirmado.getDataHoraInicio(),
                agendamentoConfirmado.getDataHoraFim()
        );
        for (Agendamento solicitacao : solicitacoesConflitantes) {
            solicitacao.recusar();
        }
        agendamentoRepository.saveAll(solicitacoesConflitantes);
    }

    @Transactional
    public void finalizar(Long agendamentoId) {
        Agendamento agendamento = buscarAgendamentoPorId(agendamentoId);
        agendamento.finalizar();
        agendamentoRepository.save(agendamento);
    }

    /**
     * Busca agendamentos por um status específico, de forma paginada.
     */
    @Transactional(readOnly = true)
    public Page<Agendamento> buscarPorStatus(StatusAgendamento status, Pageable pageable) {
        return agendamentoRepository.findByStatus(status, pageable);
    }

    private Agendamento buscarAgendamentoPorId(Long agendamentoId) {
        return agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento com ID " + agendamentoId + " não encontrado."));
    }

    @Transactional(readOnly = true)
    public Page<Agendamento> buscarAgendamentosAtivosPorSetor(Setor setor, Pageable pageable) {
        List<StatusAgendamento> statusesAtivos = List.of(StatusAgendamento.SOLICITADO, StatusAgendamento.CONFIRMADO);
        return agendamentoRepository.findBySalaSetorAndStatusIn(setor, statusesAtivos, pageable);
    }
}
