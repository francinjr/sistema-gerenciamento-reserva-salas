package com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.entities;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.DominioException;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.domain.entities.PessoaFisica;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "tb_recepcionistas")
public class Recepcionista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setor_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_recepcionista_setor"))
    private Setor setor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_fisica_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_recepcionista_pessoa_fisica"))
    private PessoaFisica pessoaFisica;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_recepcionista_usuario"))
    private Usuario usuario;


    public Recepcionista(Setor setor, PessoaFisica pessoaFisica, Usuario usuario) {
        validarSetor(setor);
        validarPessoaFisica(pessoaFisica);
        validarUsuario(usuario);

        this.setor = setor;
        this.pessoaFisica = pessoaFisica;
        this.usuario = usuario;
    }


    public void atualizar(Recepcionista recepcionistaComDadosNovos) {
        /* A recepcionista não é dona do setor, ela pertence a um setor, quando ela ou um
        admnistrador quer atualizar essa informação, ele/ela não muda os dados do setor, só
        muda a associação dela com um determinado setor */
        this.changeSetor(recepcionistaComDadosNovos.getSetor());
        this.pessoaFisica.atualizar(recepcionistaComDadosNovos.getPessoaFisica());
        this.usuario.atualizar(recepcionistaComDadosNovos.getUsuario());
    }

    public void changeSetor(Setor setor) {
        validarSetor(setor);
        this.setor = setor;
    }

    public void changePessoaFisica(PessoaFisica pessoaFisica) {
        validarPessoaFisica(pessoaFisica);
        this.pessoaFisica = pessoaFisica;
    }

    public void changeUsuario(Usuario usuario) {
        validarUsuario(usuario);
        this.usuario = usuario;
    }


    private void validarSetor(Setor setor) {
        if (setor == null) {
            throw new DominioException("O setor do recepcionista é obrigatório.");
        }
    }

    private void validarPessoaFisica(PessoaFisica pessoaFisica) {
        if (pessoaFisica == null) {
            throw new DominioException(
                    "Os dados da pessoa física são obrigatórios para o recepcionista.");
        }
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new DominioException("O usuário de acesso é obrigatório para o recepcionista.");
        }
    }

    /* Aplicando a lei de demeter para que o service não tenha que ser obrigado a conhecer a
    estrutura interna das entidades que compoem a entidade Recepcionista. Dessa forma se as estruturas
    das entidades mudarem, não será necessário mudar todos os services que fazem uso desse atributo.
    Ou seja, é um forte encapsulamento que vai garantir uma boa manutenabilidade do software e
    entregas rápidas de novas fucionalidades e mudanças.
     */
    public Long getRecepcionistaSetorId() {
        return setor.getId();
    }

    /* Aplicando a lei de demeter para que o service não tenha que ser obrigado a conhecer a
    estrutura interna das entidades que compoem a entidade Recepcionista. Dessa forma se as estruturas
    das entidades mudarem, não será necessário mudar todos os services que fazem uso desse atributo.
    Ou seja, é um forte encapsulamento que vai garantir uma boa manutenabilidade do software e
    entregas rápidas de novas fucionalidades e mudanças.
    */
    public Long getRecepcionistaPessoaFisicaId() {
        return pessoaFisica.getId();
    }

    /* Aplicando a lei de demeter para que o service não tenha que ser obrigado a conhecer a
    estrutura interna das entidades que compoem a entidade Recepcionista. Dessa forma se as estruturas
    das entidades mudarem, não será necessário mudar todos os services que fazem uso desse atributo.
    Ou seja, é um forte encapsulamento que vai garantir uma boa manutenabilidade do software e
    entregas rápidas de novas fucionalidades e mudanças.
    */
    public Long getRecepcionistaUsuarioId() {
        return usuario.getId();
    }
}