package com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.dtos;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Agendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.StatusAgendamento;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class BuscarAgendamentoDto {
    private final Long id;
    private final LocalDateTime dataHoraInicio;
    private final LocalDateTime dataHoraFim;
    private final StatusAgendamento status;
    private final Integer quantidadePessoas;
    private final BigDecimal valorTotal;
    private final String nomeSala;
    private final String nomeCliente;

    public BuscarAgendamentoDto(Agendamento agendamento) {
        this.id = agendamento.getId();
        this.dataHoraInicio = agendamento.getDataHoraInicio();
        this.dataHoraFim = agendamento.getDataHoraFim();
        this.status = agendamento.getStatus();
        this.quantidadePessoas = agendamento.getQuantidadePessoas();
        this.valorTotal = agendamento.getValorTotal();
        this.nomeSala = agendamento.getSala().getNome();
        this.nomeCliente = agendamento.getCliente().getPessoaFisica().getNome();
    }
}