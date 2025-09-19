package com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.domain.services;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.domain.entities.Agendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.domain.repositories.AgendamentoRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.domain.repositories.AgendamentoSpecification;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.dtos.FiltroRelatorioDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.dtos.RelatorioSalaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.dtos.RelatorioSetorDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Cliente;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.valueobjects.Dinheiro;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final AgendamentoRepository agendamentoRepository;

    public List<RelatorioSetorDto> gerarRelatorio(FiltroRelatorioDto filtro, Cliente cliente, Setor setor) {

        List<Specification<Agendamento>> specifications = new ArrayList<>();
        // Adiciona as especificações de filtro do formulário
        specifications.add(AgendamentoSpecification.comDataFinalizacaoEntre(
                filtro.getDataInicio() != null ? filtro.getDataInicio().atStartOfDay() : null,
                filtro.getDataFim() != null ? filtro.getDataFim().atTime(LocalTime.MAX) : null));

        specifications.add(AgendamentoSpecification.comNomeDeSetor(filtro.getNomeSetor()));

        // Adiciona as especificações de permissão baseadas no perfil do usuário
        if (cliente != null) {
            specifications.add(AgendamentoSpecification.doCliente(cliente));
        } else if (setor != null) {
            specifications.add(AgendamentoSpecification.doSetor(setor));
        }

        // Combina todas as especificações da lista com um "AND"
        Specification<Agendamento> spec = Specification.allOf(specifications);

        List<Agendamento> agendamentos = agendamentoRepository.findAll(spec);

        // Agrega os resultados para a view
        return agendamentos.stream()
                .collect(Collectors.groupingBy(ag -> ag.getSala().getSetor()))
                .entrySet().stream()
                .map(entry -> {
                    Setor s = entry.getKey();
                    List<Agendamento> agsDoSetor = entry.getValue();

                    List<RelatorioSalaDto> salasDto = agsDoSetor.stream()
                            .collect(Collectors.groupingBy(Agendamento::getSala))
                            .entrySet().stream()
                            .map(salaEntry -> {
                                Dinheiro faturamentoSala = salaEntry.getValue().stream()
                                        .map(Agendamento::getValorTotal)
                                        .reduce(new Dinheiro(0.0), Dinheiro::somar);
                                return new RelatorioSalaDto(salaEntry.getKey().getId(), salaEntry.getKey().getNome(), faturamentoSala);
                            }).toList();

                    Dinheiro faturamentoSetor = salasDto.stream()
                            .map(RelatorioSalaDto::getFaturamento)
                            .reduce(new Dinheiro(0.0), Dinheiro::somar);

                    return new RelatorioSetorDto(s.getId(), s.getNome(), faturamentoSetor, salasDto);
                }).toList();
    }
}