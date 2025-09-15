package com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SalvarSetorDto(

        @NotBlank(message = "O nome do setor não pode ser vazio ou nulo.")
        @Size(min = 3, max = 100, message = "O nome do setor deve ter entre 3 e 100 caracteres.")
        String nome,

        @Size(max = 255, message = "A descrição não pode exceder 255 caracteres.")
        String descricao

) {

    public SalvarSetorDto() {
        this("", "");
    }
}
