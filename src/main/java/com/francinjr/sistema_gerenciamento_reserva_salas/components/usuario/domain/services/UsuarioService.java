package com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.services;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.domain.valueobjects.Email;
import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.RecursoNaoEncontradoException;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.PapelUsuario;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.Usuario;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.repositories.UsuarioRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.web.dtos.SalvarUsuarioDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /*
    Para criar um usuário, recebe-se os dados do usuário e a role do usuário, dessa forma o metodo
    se torna reaproveitável se existir novos tipos de usuário no sistema.
     */
    @Transactional
    public Usuario criar(SalvarUsuarioDto dto, PapelUsuario papel) {
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());

        Usuario novoUsuario = new Usuario(dto.getEmail(), senhaCriptografada, papel);
        return usuarioRepository.save(novoUsuario);
    }

    @Transactional(readOnly = true)
    public Page<Usuario> buscarPorEmail(String email, Pageable pageable) {
        if (email != null && !email.isBlank()) {
            return usuarioRepository.findByEmailValorContainingIgnoreCase(email, pageable);
        } else {
            return usuarioRepository.findAll(pageable);
        }
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário com ID " + id + " não encontrado."));
    }


    @Transactional
    public Usuario atualizar(Long id, SalvarUsuarioDto dto, PapelUsuario papel) {
        Usuario usuarioExistente = buscarPorId(id);
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());

        Usuario usuarioComDadosParaAtualizar = new Usuario(dto.getEmail(), senhaCriptografada, papel);
        usuarioExistente.atualizar(usuarioComDadosParaAtualizar);

        return usuarioRepository.save(usuarioExistente);
    }

    @Transactional
    public void excluir(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuário com ID " + id + " não encontrado.");
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Email email = new Email(username);
        return usuarioRepository.findByEmailValor(email.getValor())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + username));
    }
}