package com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.web.dtos;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BuscarSalaDto {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private String descricao;
    private String nomeSetor;
    private Integer capacidadeMaxima;
}