package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.services;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.NegocioException;
import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.RecursoNaoEncontradoException;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Agendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Cliente;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.StatusAgendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.repositories.AgendamentoRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos.SalvarAgendamentoDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.entities.Sala;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.services.SalaService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
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
                dto.getDataHoraInicio(), dto.getDataHoraFim(), sala, cliente, dto.getQuantidadePessoas());
        return agendamentoRepository.save(novoAgendamento);
    }

    @Transactional
    public void cancelarSolicitacao(Long agendamentoId, Cliente cliente) {
        // 1. Busca o agendamento no banco
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação de agendamento com ID " + agendamentoId + " não encontrada."));

        // 2. Valida se o agendamento pertence ao cliente logado
        if (!agendamento.pertenceAoCliente(cliente)) {
            // Lança uma exceção de segurança para evitar que um cliente cancele o agendamento de outro
            throw new SecurityException("Acesso negado: Você não tem permissão para cancelar esta solicitação.");
        }

        // 3. Delega a lógica de negócio para a própria entidade
        agendamento.cancelar();

        // 4. Salva as alterações no banco de dados
        agendamentoRepository.save(agendamento);
    }


    @Transactional(readOnly = true)
    public Agendamento buscarSolicitacaoPorId(Long agendamentoId, Cliente cliente) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação com ID " + agendamentoId + " não encontrada."));

        if (!agendamento.pertenceAoCliente(cliente)) {
            throw new SecurityException("Acesso negado: esta solicitação não pertence a você.");
        }
        if (agendamento.getStatus() != StatusAgendamento.SOLICITADO) {
            throw new NegocioException("Esta reserva não está mais no status de 'SOLICITADO'.");
        }

        return agendamento;
    }
}
