package com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.entities;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.DominioException;
import com.francinjr.sistema_gerenciamento_reserva_salas.commons.domain.valueobjects.Dinheiro;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import jakarta.persistence.*;
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

    @Column(name = "capacidade_maxima", nullable = false)
    private Integer capacidadeMaxima;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setor_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sala_para_setor"))
    private Setor setor;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "caixa", nullable = false))
    private Dinheiro caixa;

    public Sala(Long id, String nome, Dinheiro preco, String descricao, Setor setor, Integer capacidadeMaxima) {
        this.validarNome(nome);
        this.validarPreco(preco);
        this.validarSetor(setor);
        this.validarCapacidadeMaxima(capacidadeMaxima);

        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.setor = setor;
        this.capacidadeMaxima = capacidadeMaxima;
        this.caixa = new Dinheiro(0.0);
    }

    public void changeNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }

    public void changePreco(Dinheiro preco) {
        validarPreco(preco);
        this.preco = preco;
    }

    public void changeDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void changeSetor(Setor setor) {
        validarSetor(setor);
        this.setor = setor;
    }

    public void changeCapacidadeMaxima(Integer capacidadeMaxima) {
        this.validarCapacidadeMaxima(capacidadeMaxima);
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public void atualizar(Sala salaComDadosNovos) {
        this.changeNome(salaComDadosNovos.getNome());
        this.changePreco(salaComDadosNovos.getPreco());
        this.changeDescricao(salaComDadosNovos.getDescricao());
        this.changeSetor(salaComDadosNovos.getSetor());
        this.changeCapacidadeMaxima(salaComDadosNovos.getCapacidadeMaxima());
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DominioException("O nome da sala não pode ser vazio ou nulo.");
        }
    }

    private void validarPreco(Dinheiro preco) {
        if (preco == null) {
            throw new DominioException("O preço da sala é obrigatório.");
        }
    }

    private void validarSetor(Setor setor) {
        if (setor == null || setor.getId() == null) {
            throw new DominioException("A sala deve estar associada a um setor válido.");
        }
    }

    private void validarCapacidadeMaxima(Integer capacidade) {
        if (capacidade == null || capacidade <= 0) {
            throw new DominioException("A capacidade máxima da sala deve ser um número positivo.");
        }
    }
}