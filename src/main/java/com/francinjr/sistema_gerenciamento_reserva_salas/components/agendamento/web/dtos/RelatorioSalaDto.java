package com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.dtos;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.domain.valueobjects.Dinheiro;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RelatorioSalaDto {
    private Long id;
    private String nome;
    private Dinheiro faturamento;
}
