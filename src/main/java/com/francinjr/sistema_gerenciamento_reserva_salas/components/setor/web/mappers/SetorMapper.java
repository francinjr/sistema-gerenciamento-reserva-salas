package com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.web.mappers;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.web.dtos.BuscarSetorDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class SetorMapper {

    public BuscarSetorDto paraDto(Setor setor) {
        if (setor == null) {
            return null;
        }
        return new BuscarSetorDto(setor.getId(), setor.getNome(), setor.getDescricao());
    }


    public Page<BuscarSetorDto> paginaEntidadeParaPaginaDto(Page<Setor> page) {
        if (page == null) {
            return null;
        }
        return page.map(this::paraDto);
    }
}