package com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.services;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.RecursoNaoEncontradoException;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.domain.entities.PessoaFisica;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.domain.services.PessoaFisicaService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.entities.Recepcionista;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.repositories.RecepcionistaRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.web.dtos.SalvarRecepcionistaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.services.SetorService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.PapelUsuario;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.Usuario;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecepcionistaService {

    private final RecepcionistaRepository recepcionistaRepository;
    private final PessoaFisicaService pessoaFisicaService;
    private final UsuarioService usuarioService;
    private final SetorService setorService;

    @Transactional
    public Recepcionista criar(SalvarRecepcionistaDto dto) {
        PessoaFisica pessoaFisica = pessoaFisicaService.criar(dto.getPessoaFisica());
        Usuario usuario = usuarioService.criar(dto.getUsuario(), PapelUsuario.RECEPCIONISTA);

        Setor setor = setorService.buscarPorId(dto.getSetorId());

        Recepcionista novoRecepcionista = new Recepcionista(setor, pessoaFisica, usuario);
        return recepcionistaRepository.save(novoRecepcionista);
    }

    @Transactional(readOnly = true)
    public Page<Recepcionista> buscar(String nome, Pageable pageable) {
        if (nome != null && !nome.isBlank()) {
            return recepcionistaRepository.findByPessoaFisicaNomeContainingIgnoreCaseWithDetails(
                    nome, pageable);
        } else {
            return recepcionistaRepository.findAllWithDetails(pageable);
        }
    }


    @Transactional(readOnly = true)
    public Recepcionista buscarPorId(Long id) {
        return recepcionistaRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Recepcionista com ID " + id + " não encontrado."));
    }


    @Transactional
    public Recepcionista atualizar(Long id, SalvarRecepcionistaDto dto) {
        Recepcionista recepcionistaExistente = buscarPorId(id);

        PessoaFisica pessoaFisicaAtualizada = pessoaFisicaService.atualizar(
                recepcionistaExistente.getRecepcionistaPessoaFisicaId(), dto.getPessoaFisica());

        Usuario usuarioAtualizado = usuarioService.atualizar(
                recepcionistaExistente.getRecepcionistaUsuarioId(), dto.getUsuario(), PapelUsuario.RECEPCIONISTA);

        /* Buscando o setor com o setorId que veio no dto, para caso o Administrador mude o setor
        da recepcionista
         */
        Setor setorAtualizado = setorService.buscarPorId(dto.getSetorId());

        Recepcionista recepcionistaComDadosParaAtualizar = new Recepcionista(setorAtualizado,
                pessoaFisicaAtualizada, usuarioAtualizado);

        recepcionistaExistente.atualizar(recepcionistaComDadosParaAtualizar);
        return recepcionistaRepository.save(recepcionistaExistente);
    }

    /**
     * Exclui um Recepcionista. A configuração Cascade na entidade pode cuidar da exclusão do
     * usuário, se desejado.
     */
    @Transactional
    public void excluir(Long id) {
        if (!recepcionistaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException(
                    "Recepcionista com ID " + id + " não encontrado.");
        }
        // Se houver outras entidades dependentes de Recepcionista (ex: Agendamentos),
        // uma exceção de integridade será lançada e tratada pelo GlobalExceptionHandler.
        recepcionistaRepository.deleteById(id);
    }
}