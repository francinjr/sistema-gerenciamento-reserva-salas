package com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.web.dtos;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.web.dtos.BuscarPessoaFisicaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.web.dtos.BuscarSetorDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.web.dtos.BuscarUsuarioDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BuscarRecepcionistaDto {
    private Long id;
    private BuscarSetorDto setor;
    private BuscarPessoaFisicaDto pessoaFisica;
    private BuscarUsuarioDto usuario;
}
