package com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.DominioException;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.entities.Sala;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "tb_setores")
public class Setor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", unique = true, nullable = false)
    private String nome;

    @Column(name = "descricao", nullable = true)
    private String descricao;

    @OneToMany(
            mappedBy = "setor",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Sala> salas = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusSetor status;

    public Setor(Long id, String nome, String descricao, Set<Sala> salas) {
        this.validarNome(nome);

        this.nome = nome;
        this.descricao = descricao;
        this.salas = salas;
        this.status = StatusSetor.FECHADO;
    }


    public void validarNome(String nome) {
        if (nome == null) {
            throw new DominioException("O nome da sala não pode ser null");
        }

        if (nome.isBlank()) {
            throw new DominioException("O nome não pode estar em branco.");
        }
    }

    public void adicionarSala(Sala sala) {
        if (sala == null) {
            throw new DominioException("Não é permitido adicionar uma sala null para um setor.");
        }

        this.salas.add(sala);
    }

    public void removerSala(Sala sala) {
        this.salas.remove(sala);
    }

    /* Ao invés de setters, são usados métodos nominados changeAtributo, pois eles deixam
     claro a intenção do comportamento que executa, ao contrário de usar setter, que não
     diz muitas coisa, evidenciando o encapsulamento adequado, pois deve haver validações
     */
    public void changeNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }

    public void changeDescricao(String descricao) {
        this.descricao = descricao;
    }

    /*
    Para atualizar os dados da entidade, é utilizado o metodo atualizar, garantindo o
    encapsulamento, e deixando uma claro a intenção de negócio, que no caso é a solicitação
    de atualização dos valores por parte do ator que executa a operação, garantindo o
    encapsulamento, assim toda aplicação se benefecia disso, pois não precisa repetir código,
    obedecendo também a regra de negócio do que pode ou não ser atualizado no sistema.
     */
    public void atualizar(Setor setorComDadosParaAtualizar) {
        this.changeNome(setorComDadosParaAtualizar.getNome());
        this.changeDescricao(setorComDadosParaAtualizar.getDescricao());
    }

    public void abrir() {
        if (this.status == StatusSetor.ABERTO) {
            throw new DominioException("O setor já está aberto.");
        }
        this.status = StatusSetor.ABERTO;
    }

    public void fechar() {
        if (this.status == StatusSetor.FECHADO) {
            throw new DominioException("O setor já está fechado.");
        }
        this.status = StatusSetor.FECHADO;
    }
}
