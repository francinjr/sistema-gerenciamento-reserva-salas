package com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.domain.entities;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.domain.valueobjects.Cpf;
import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.DominioException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "tb_pessoas_fisicas")
public class PessoaFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "telefone", unique = true, nullable = false)
    private String telefone;

    @Embedded
    @AttributeOverride(
            name = "valor",
            column = @Column(name = "cpf", unique = true, nullable = false)
    )
    private Cpf cpf;

    public PessoaFisica(String nome, String cpf, String telefone) {
        validarNome(nome);

        /* O Cpf vem como uma string(primitivo) e quem sabe criar esse conceito do dominio
        é ele mesmo, ou seja o service nao precisa saber como instanciar um value object como o
        Cpf, quem sabe o momento correto de fazer isso é o PessoaFisica, e quem sabe como instanciar
        é o próprio value object Cpf.
         */
        this.cpf = new Cpf(cpf);
        validarCpf(this.cpf);
        validarTelefone(telefone);

        this.nome = nome;
        this.telefone = telefone;
    }

    public void atualizar(PessoaFisica pessoaComDadosNovos) {
        /*
        Regra de dominio estabelecida para garantir a segurança: Um usuário não pode mudar o cpf.
        Como está sendo garantido o encapsulamento, o metodo atualizar nao chama o changeCpf, a
        menos que exista uma regra de negocio especifica baseado em alguma condicao em outro
        local que permita isso.
         */
        this.changeNome(pessoaComDadosNovos.getNome());
        this.changeTelefone(pessoaComDadosNovos.getTelefone());
    }

    public void changeNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }

    public void changeCpf(Cpf cpf) {
        validarCpf(cpf);
        this.cpf = cpf;
    }

    public void changeTelefone(String telefone) {
        validarTelefone(telefone);
        this.telefone = telefone;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DominioException("O nome da pessoa não pode ser vazio ou nulo.");
        }
    }

    /* Metodo para validar o cpf que garante uma regra de negocio estabelecida: Um usuario deve
    obrigatoriamente ter um cpf, as validacoes de cpf estao dentre dele, pois o memso sabe como se
    validar, porem a validacao de que deve haver um cpf para uma PessoaFisica faz parte propria
    pessoa fisica.
     */
    private void validarCpf(Cpf cpf) {
        if (cpf == null) {
            throw new DominioException("O CPF da pessoa é obrigatório.");
        }
    }

    private void validarTelefone(String telefone) {
        if (telefone == null || telefone.isBlank()) {
            throw new DominioException("O telefone da pessoa é obrigatório.");
        }
    }
}