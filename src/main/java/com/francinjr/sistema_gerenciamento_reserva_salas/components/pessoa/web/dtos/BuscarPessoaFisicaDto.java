package com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BuscarPessoaFisicaDto {

    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
}
