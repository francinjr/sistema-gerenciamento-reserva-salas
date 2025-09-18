package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Agendamento;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import lombok.Getter;


@Getter
public class DetalhesAgendamentoDto {
    private final Long agendamentoId;
    private final Integer quantidadePessoas;
    private final BigDecimal valorTotal;
    private final BigDecimal valorSinal;
    private final BigDecimal valorFinalizacao;
    private final String nomeSala;
    private final int capacidadeSala;
    private final String nomeSetor;
    private final String dataHoraInicio;
    private final String dataHoraFim;

    public DetalhesAgendamentoDto(Agendamento agendamento) {
        this.agendamentoId = agendamento.getId();
        this.quantidadePessoas = agendamento.getQuantidadePessoas();
        this.valorTotal = agendamento.getValorTotal();
        this.valorSinal = agendamento.getValorSinal();
        this.valorFinalizacao = agendamento.getValorFinalizacao();
        this.nomeSala = agendamento.getSala().getNome();
        this.capacidadeSala = agendamento.getSala().getCapacidadeMaxima();
        this.nomeSetor = agendamento.getSala().getSetor().getNome();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        this.dataHoraInicio = agendamento.getDataHoraInicio().format(formatter);
        this.dataHoraFim = agendamento.getDataHoraFim().format(formatter);
    }
}
