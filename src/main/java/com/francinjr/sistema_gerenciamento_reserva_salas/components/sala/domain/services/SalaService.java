package com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.services;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.RecursoNaoEncontradoException;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.entities.Sala;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.repositories.SalaRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.valueobjects.Dinheiro;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.web.dtos.SalvarSalaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.services.SetorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SalaService {
    private final SalaRepository salaRepository;
    private final SetorService setorService;

    @Transactional
    public Sala criar(SalvarSalaDto dto) {
        Setor setorAssociado = setorService.buscarPorId(dto.getSetorId());
        Sala novaSala = new Sala(
                null,
                dto.getNome(),
                new Dinheiro(dto.getPreco()),
                dto.getDescricao(),
                setorAssociado,
                dto.getCapacidadeMaxima()
        );
        return salaRepository.save(novaSala);
    }

    @Transactional(readOnly = true)
    public Page<Sala> buscar(String nome, Pageable pageable) {
        if (nome != null && !nome.isBlank()) {
            return salaRepository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            return salaRepository.findAll(pageable);
        }
    }

    @Transactional(readOnly = true)
    public Sala buscarPorId(Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sala com ID " + id + " não encontrada."));
    }

    @Transactional
    public Sala atualizar(Long id, SalvarSalaDto dto) {
        Sala salaExistente = buscarPorId(id);
        Setor setorAssociado = setorService.buscarPorId(dto.getSetorId());

        Sala dadosParaAtualizar = new Sala(
                id,
                dto.getNome(),
                new Dinheiro(dto.getPreco()),
                dto.getDescricao(),
                setorAssociado,
                dto.getCapacidadeMaxima()
        );
        salaExistente.atualizar(dadosParaAtualizar);

        return salaRepository.save(salaExistente);
    }

    @Transactional
    public void excluir(Long id) {
        if (!salaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Sala com ID " + id + " não encontrada.");
        }
        salaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Sala> buscarPorSetor(Setor setor) {
        return salaRepository.findBySetor(setor);
    }
}
