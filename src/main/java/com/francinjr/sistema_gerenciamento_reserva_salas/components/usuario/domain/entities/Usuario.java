package com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.domain.valueobjects.Email;
import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.DominioException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "tb_usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(
            name = "valor",
            column = @Column(name = "email", unique = true, nullable = false)
    )
    private Email email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "papel", nullable = false)
    private PapelUsuario papel;

    public Usuario(String email, String senha, PapelUsuario papel) {
        this.email = new Email(email);
        validarEmail(this.email);

        validarSenha(senha);
        validarPapel(papel);

        this.senha = senha;
        this.papel = papel;
    }

    public void atualizar(Usuario usuarioComDadosNovos) {
        this.changeEmail(usuarioComDadosNovos.getEmail());
        /*
            O metodo atualizar não deve mudar a senha do usuário
         */
        this.changeEmail(usuarioComDadosNovos.getEmail());
        this.changePapel(usuarioComDadosNovos.getPapel());
    }

    public void changeEmail(Email email) {
        validarEmail(email);
        this.email = email;
    }

    public void changePapel(PapelUsuario papel) {
        validarPapel(papel);
        this.papel = papel;
    }

    // Regra de dominío da entidade Usuario: É obrigatório que um usuário tenha um email
    private void validarEmail(Email email) {
        if (email == null) {
            throw new DominioException("O e-mail do usuário é obrigatório.");
        }
    }

    // Regra de dominío da entidade Usuario: É obrigatório que um usuário tenha uma senha
    private void validarSenha(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new DominioException("A senha do usuário é obrigatória.");
        }
    }

    // Regra de dominío da entidade Usuario: É obrigatório que um usuário tenha um papel(role)
    private void validarPapel(PapelUsuario papel) {
        if (papel == null) {
            throw new DominioException("O papel (role) do usuário é obrigatório.");
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.papel == PapelUsuario.ADMINISTRADOR) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"), new SimpleGrantedAuthority("ROLE_RECEPCIONISTA"), new SimpleGrantedAuthority("ROLE_CLIENTE"));
        } else if (this.papel == PapelUsuario.RECEPCIONISTA) {
            return List.of(new SimpleGrantedAuthority("ROLE_RECEPCIONISTA"), new SimpleGrantedAuthority("ROLE_CLIENTE"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"));
        }
    }

    @Override
    public String getUsername() {
        return this.email.getValor();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}