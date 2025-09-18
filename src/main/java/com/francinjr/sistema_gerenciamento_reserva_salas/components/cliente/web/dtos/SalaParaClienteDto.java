package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.entities.Sala;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class SalaParaClienteDto {
    private final Long id;
    private final String nome;
    private final BigDecimal preco;
    private final Integer capacidadeMaxima;

    private final String statusAgendamento;
    private final Long agendamentoId;

    public SalaParaClienteDto(Sala sala, String statusAgendamento, Long agendamentoId) {
        this.id = sala.getId();
        this.nome = sala.getNome();
        this.preco = sala.getPreco().getValor();
        this.capacidadeMaxima = sala.getCapacidadeMaxima();
        this.statusAgendamento = statusAgendamento;
        this.agendamentoId = agendamentoId;
    }
}
