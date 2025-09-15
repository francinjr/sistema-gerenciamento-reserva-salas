package com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.entities;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.DominioException;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.valueobjects.Dinheiro;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "tb_salas")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", unique = true, nullable = false)
    private String nome;

    @Embedded
    @AttributeOverride(
            name = "valor",
            column = @Column(name = "preco", nullable = false)
    )
    private Dinheiro preco;

    @Column(name = "descricao", nullable = true)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "setor_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_sala_setor")
    )
    private Setor setor;

    public Sala(Long id, String nome, Double preco, String descricao, Setor setor) {
        this.validarNome(nome);

        this.id = id;
        this.nome = nome;
        this.preco = new Dinheiro(preco);
        this.descricao = descricao;
        this.setor = setor;
    }

    public BigDecimal getPreco() {
        return this.preco.getValor();
    }

    public void validarNome(String nome) {
        if(nome == null) {
            throw new DominioException("O nome da sala não pode ser null");
        }

        if(nome.isBlank()) {
            throw new DominioException("O nome não pode estar em branco.");
        }
    }
}
