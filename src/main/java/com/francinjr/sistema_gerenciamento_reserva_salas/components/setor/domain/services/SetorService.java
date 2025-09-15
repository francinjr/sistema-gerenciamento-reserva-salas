package com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.services;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.repositories.SetorRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.web.dtos.SalvarSetorDto;
import lombok.RequiredArgsConstructor;
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

}
