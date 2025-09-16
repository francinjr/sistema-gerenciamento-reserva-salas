package com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.services;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.RecursoEmUsoException;
import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.RecursoNaoEncontradoException;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.repositories.SetorRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.web.dtos.SalvarSetorDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SetorService {

    private final SetorRepository setorRepository;

    @Transactional
    public Setor criar(SalvarSetorDto dadosParaSalvar) {
        Setor entidade = new Setor(null, dadosParaSalvar.nome(), dadosParaSalvar.descricao(), null);

        return setorRepository.save(entidade);
    }

    @Transactional(readOnly = true)
    public Page<Setor> buscar(String nome, Pageable pageable) {
        if (nome != null && !nome.isBlank()) {
            return setorRepository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            return setorRepository.findAll(pageable);
        }
    }

    @Transactional(readOnly = true)
    public Setor buscarPorId(Long id) {
        return setorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Setor com ID " + id + " não encontrado."));
    }

    @Transactional
    public Setor atualizar(Long id, SalvarSetorDto dto) {
        Setor entidadeExistente = buscarPorId(id);

        Setor entidadeComDadosParaAtualizar = new Setor(id, dto.nome(), dto.descricao(), entidadeExistente.getSalas());
        entidadeExistente.atualizar(entidadeComDadosParaAtualizar);

        return setorRepository.save(entidadeExistente);
    }

    @Transactional
    public void excluir(Long id) {
        Setor setor = buscarPorId(id);
        try {
            setorRepository.delete(setor);
            setorRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new RecursoEmUsoException(
                    "Não é possível excluir o setor '" + setor.getNome() + "' pois existem salas associadas a ele."
            );
        }
    }

    @Transactional(readOnly = true)
    public List<Setor> buscarTodos() {
        return setorRepository.findAll();
    }
}
