package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Agendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.StatusAgendamento;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class BuscarAgendamentoDto {
    private final Long id;
    private final StatusAgendamento status;
    private final Integer quantidadePessoas;
    private final BigDecimal valorTotal;
    private final String nomeSala;
    private final String nomeCliente;
    private final String dataHoraInicio;
    private final String dataHoraFim;
    private final BigDecimal valorFinalizacao;

    public BuscarAgendamentoDto(Agendamento agendamento) {
        this.id = agendamento.getId();
        this.status = agendamento.getStatus();
        this.quantidadePessoas = agendamento.getQuantidadePessoas();
        this.valorTotal = agendamento.getValorTotal();
        this.nomeSala = agendamento.getSala().getNome();
        this.nomeCliente = agendamento.getCliente().getPessoaFisica().getNome();

        // Converte o LocalDateTime para uma String no formato desejado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        this.dataHoraInicio = agendamento.getDataHoraInicio().format(formatter);
        this.dataHoraFim = agendamento.getDataHoraFim().format(formatter);
        this.valorFinalizacao = agendamento.getValorFinalizacao();
    }
}