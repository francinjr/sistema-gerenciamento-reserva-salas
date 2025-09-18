package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.mappers;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.dtos.BuscarAgendamentoDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Agendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos.DetalhesAgendamentoDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {

    public BuscarAgendamentoDto paraDto(Agendamento agendamento) {
        if (agendamento == null) {
            return null;
        }
        return new BuscarAgendamentoDto(agendamento);
    }

    public Page<BuscarAgendamentoDto> paginaEntidadeParaPaginaDto(Page<Agendamento> page) {
        if (page == null) {
            return null;
        }
        return page.map(this::paraDto);
    }

    public DetalhesAgendamentoDto paraDetalhesDto(Agendamento agendamento) {
        if (agendamento == null) return null;
        return new DetalhesAgendamentoDto(agendamento);
    }
}
