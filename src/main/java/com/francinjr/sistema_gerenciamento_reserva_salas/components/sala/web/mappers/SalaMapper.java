package com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.web.mappers;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.entities.Sala;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.web.dtos.BuscarSalaDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class SalaMapper {

    public BuscarSalaDto paraDto(Sala sala) {
        if (sala == null) {
            return null;
        }

        return new BuscarSalaDto(sala.getId(), sala.getNome(), sala.getPreco().getValor(), sala.getDescricao(), sala.getSetor()
                .getNome(), sala.getCapacidadeMaxima());
    }


    public Page<BuscarSalaDto> paginaEntidadeParaPaginaDto(Page<Sala> page) {
        if (page == null) {
            return null;
        }
        return page.map(this::paraDto);
    }

    public List<BuscarSalaDto> paraListaDto(List<Sala> salas) {
        if (salas == null) {
            return null;
        }
        return salas.stream()
                .map(this::paraDto)
                .collect(Collectors.toList());
    }
}