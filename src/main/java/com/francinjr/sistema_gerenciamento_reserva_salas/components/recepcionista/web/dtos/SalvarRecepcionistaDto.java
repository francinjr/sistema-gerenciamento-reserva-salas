package com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.web.dtos;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.web.dtos.SalvarPessoaFisicaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.web.dtos.SalvarUsuarioDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; // ✅ Importar o Setter

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalvarRecepcionistaDto {

    @NotNull
    @Valid
    private SalvarPessoaFisicaDto pessoaFisica;

    @NotNull
    @Valid
    private SalvarUsuarioDto usuario;

    @NotNull(message = "O setor é obrigatório.")
    private Long setorId;
}