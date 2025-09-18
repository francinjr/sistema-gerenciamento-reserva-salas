package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SetorComSalasDto {
    private final Long id;
    private final String nome;
    private final String descricao;
    private final List<SalaParaClienteDto> salas;
}
