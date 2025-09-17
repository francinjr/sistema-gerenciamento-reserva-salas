package com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.web.dtos;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.PapelUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BuscarUsuarioDto {

    private final Long id;
    private final String email;
    private final PapelUsuario papel;
}