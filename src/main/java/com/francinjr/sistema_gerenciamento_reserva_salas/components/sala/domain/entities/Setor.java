package com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.entities;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.DomainException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "tb_setor")
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

    public Setor(Long id, String nome, String descricao, Set<Sala> salas) {
        this.validarNome(nome);

        this.nome = nome;
        this.descricao = descricao;
        this.salas = salas;
    }


    public void validarNome(String nome) {
        if(nome == null) {
            throw new DomainException("O nome da sala não pode ser null");
        }

        if(nome.isBlank()) {
            throw new DomainException("O nome não pode estar em branco.");
        }
    }

    public void adicionarSala(Sala sala) {
        if(sala == null) {
            throw new DomainException("Não é permitido adicionar uma sala null para um setor.");
        }

        this.salas.add(sala);
    }

    public void removerSala(Sala sala) {
        this.salas.remove(sala);
    }
}
