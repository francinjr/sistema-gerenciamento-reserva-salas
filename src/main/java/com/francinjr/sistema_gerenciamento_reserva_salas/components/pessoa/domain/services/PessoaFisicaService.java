package com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.domain.services;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.domain.valueobjects.Cpf;
import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.RecursoNaoEncontradoException;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.domain.entities.PessoaFisica;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.domain.repositories.PessoaFisicaRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.web.dtos.SalvarPessoaFisicaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PessoaFisicaService {

    private final PessoaFisicaRepository pessoaFisicaRepository;

    @Transactional
    public PessoaFisica criar(SalvarPessoaFisicaDto dto) {

        PessoaFisica novaPessoa = new PessoaFisica(dto.getNome(), dto.getCpf(), dto.getTelefone());
        return pessoaFisicaRepository.save(novaPessoa);
    }

    @Transactional(readOnly = true)
    public Page<PessoaFisica> buscar(String nome, Pageable pageable) {
        if (nome != null && !nome.isBlank()) {
            return pessoaFisicaRepository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            return pessoaFisicaRepository.findAll(pageable);
        }
    }

    @Transactional(readOnly = true)
    public PessoaFisica buscarPorId(Long id) {
        return pessoaFisicaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa com ID " + id + " não encontrada."));
    }

    @Transactional
    public PessoaFisica atualizar(Long id, SalvarPessoaFisicaDto dto) {
        PessoaFisica entidadeExistente = buscarPorId(id);

        PessoaFisica pessoaFisicaComDadosParaAtualizar = new PessoaFisica(dto.getNome(), dto.getCpf(), dto.getTelefone());
        entidadeExistente.atualizar(pessoaFisicaComDadosParaAtualizar);

        return pessoaFisicaRepository.save(entidadeExistente);
    }

    @Transactional
    public void excluir(Long id) {
        if (!pessoaFisicaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Pessoa com ID " + id + " não encontrada.");
        }
        pessoaFisicaRepository.deleteById(id);
    }
}