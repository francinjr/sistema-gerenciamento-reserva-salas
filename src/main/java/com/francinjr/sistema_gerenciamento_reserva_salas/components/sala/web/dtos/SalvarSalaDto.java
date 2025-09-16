package com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record SalvarSalaDto(
        @NotBlank(message = "O nome da sala não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O nome da sala deve ter entre 3 e 100 caracteres.")
        String nome,

        @NotNull(message = "O preço é obrigatório.")
        @PositiveOrZero(message = "O preço não pode ser negativo.")
        Double preco,

        @Size(max = 255, message = "A descrição não pode exceder 255 caracteres.")
        String descricao,

        @NotNull(message = "O ID do setor é obrigatório.")
        Long setorId
) {
    public SalvarSalaDto() {
        this("", 0.0, "", null);
    }
}