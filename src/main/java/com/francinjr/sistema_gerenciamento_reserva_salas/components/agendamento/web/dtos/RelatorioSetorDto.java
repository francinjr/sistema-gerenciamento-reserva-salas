package com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.dtos;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.valueobjects.Dinheiro;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class RelatorioSetorDto {
    private Long id;
    private String nome;
    private Dinheiro faturamentoTotal;
    private List<RelatorioSalaDto> salas;
}
