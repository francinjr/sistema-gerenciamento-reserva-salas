package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.services;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.domain.entities.Agendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Cliente;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.domain.entities.StatusAgendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos.SalaParaClienteDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos.SetorComSalasDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.repositories.SetorRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final SetorRepository setorRepository;

    @Transactional(readOnly = true)
    public List<SetorComSalasDto> getDadosDashboardCliente(Cliente cliente, String nomeSetor) {
        List<Setor> setores;

        if (nomeSetor != null && !nomeSetor.isBlank()) {
            setores = setorRepository.findAllAbertosWithSalasByNomeContainingIgnoreCase(nomeSetor);
        } else {
            setores = setorRepository.findAllAbertosWithSalas();
        }

        return setores.stream().map(setor -> {
            List<SalaParaClienteDto> salasDto = setor.getSalas().stream().map(sala -> {

                Optional<Agendamento> agendamentoAtivo = cliente.getAgendamentos().stream()
                        .filter(ag -> ag.getSala().getId().equals(sala.getId()) &&
                                (ag.getStatus() == StatusAgendamento.SOLICITADO
                                        || ag.getStatus() == StatusAgendamento.CONFIRMADO))
                        .findFirst();

                String status = agendamentoAtivo.map(ag -> ag.getStatus().name()).orElse(null);
                Long agendamentoId = agendamentoAtivo.map(Agendamento::getId).orElse(null);

                return new SalaParaClienteDto(sala, status, agendamentoId);

            }).collect(Collectors.toList());

            return new SetorComSalasDto(setor.getId(), setor.getNome(), setor.getDescricao(),
                    salasDto);
        }).collect(Collectors.toList());
    }
}
