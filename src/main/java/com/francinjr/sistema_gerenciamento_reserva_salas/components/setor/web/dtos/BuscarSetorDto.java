package com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.web.dtos;


import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.StatusSetor;
import lombok.Getter;

@Getter
public class BuscarSetorDto {
    private final Long id;
    private final String nome;
    private final String descricao;
    private final StatusSetor status;

    public BuscarSetorDto(Setor setor) {
        this.id = setor.getId();
        this.nome = setor.getNome();
        this.descricao = setor.getDescricao();
        this.status = setor.getStatus();
    }
}