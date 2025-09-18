package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.web.dtos.SalvarPessoaFisicaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.web.dtos.SalvarUsuarioDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalvarClienteDto {

    @NotNull
    @Valid
    private SalvarPessoaFisicaDto pessoaFisica;

    @NotNull
    @Valid
    private SalvarUsuarioDto usuario;
}
