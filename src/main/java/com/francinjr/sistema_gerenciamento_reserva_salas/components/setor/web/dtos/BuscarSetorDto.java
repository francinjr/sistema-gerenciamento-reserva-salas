package com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BuscarSetorDto {
    private final Long id;
    private final String nome;
    private final String descricao;
}
