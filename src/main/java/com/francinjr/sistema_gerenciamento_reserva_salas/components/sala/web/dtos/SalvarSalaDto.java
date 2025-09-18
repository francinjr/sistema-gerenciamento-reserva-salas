package com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.web.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalvarSalaDto {

    @NotBlank(message = "O nome da sala não pode ser vazio.")
    @Size(min = 3, max = 100)
    private String nome;

    @NotNull(message = "O preço é obrigatório.")
    @PositiveOrZero(message = "O preço não pode ser negativo.")
    private Double preco;

    @Size(max = 255)
    private String descricao;

    @NotNull(message = "O setor é obrigatório.")
    private Long setorId;

    @NotNull(message = "A capacidade máxima é obrigatória.")
    @Min(value = 1, message = "A capacidade máxima deve ser de no mínimo 1.")
    private Integer capacidadeMaxima;
}