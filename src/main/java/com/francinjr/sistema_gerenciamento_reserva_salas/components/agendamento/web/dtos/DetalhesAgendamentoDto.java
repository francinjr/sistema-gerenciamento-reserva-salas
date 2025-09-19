package com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.dtos;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.domain.entities.Agendamento;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Getter
public class DetalhesAgendamentoDto {
    private final Long agendamentoId;
    private final Integer quantidadePessoas;
    private final String nomeSala;
    private final int capacidadeSala;
    private final String nomeSetor;
    private final String dataHoraInicio;
    private final String dataHoraFim;

    // mas a forma como são preenchidos foi ajustada.
    private final BigDecimal valorTotal;
    private final BigDecimal valorSinal;
    private final BigDecimal valorFinalizacao;

    public DetalhesAgendamentoDto(Agendamento agendamento) {
        this.agendamentoId = agendamento.getId();
        this.quantidadePessoas = agendamento.getQuantidadePessoas();
        this.nomeSala = agendamento.getSala().getNome();
        this.capacidadeSala = agendamento.getSala().getCapacidadeMaxima();
        this.nomeSetor = agendamento.getSala().getSetor().getNome();

        this.valorTotal = agendamento.getValorTotal().getValor();
        this.valorSinal = agendamento.getValorSinal().getValor();
        this.valorFinalizacao = agendamento.getValorFinalizacao().getValor();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        this.dataHoraInicio = agendamento.getDataHoraInicio().format(formatter);
        this.dataHoraFim = agendamento.getDataHoraFim().format(formatter);
    }
}