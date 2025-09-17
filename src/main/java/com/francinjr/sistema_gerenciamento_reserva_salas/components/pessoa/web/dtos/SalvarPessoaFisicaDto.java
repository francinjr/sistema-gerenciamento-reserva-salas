package com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; // ✅ Importar o Setter

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalvarPessoaFisicaDto {

    @NotBlank(message = "O nome não pode ser vazio.")
    @Size(min = 3, max = 255)
    private String nome;

    @NotBlank(message = "O CPF не pode ser vazio.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos.")
    private String cpf;

    @NotBlank(message = "O telefone não pode ser vazio.")
    @Size(min = 10, max = 15)
    private String telefone;
}