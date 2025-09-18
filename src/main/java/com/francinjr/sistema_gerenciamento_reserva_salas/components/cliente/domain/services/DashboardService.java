package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.services;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Agendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Cliente;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.StatusAgendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos.SalaParaClienteDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos.SetorComSalasDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.repositories.SetorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DashboardService {
    private final SetorRepository setorRepository;

    @Transactional(readOnly = true)
    public List<SetorComSalasDto> getDadosDashboardCliente(Cliente cliente, String nomeSetor) {
        List<Setor> setores;

        // Lógica de filtragem: Se um nome de setor foi fornecido, filtra.
        // Senão, busca todos, utilizando os métodos otimizados do repositório.
        if (nomeSetor != null && !nomeSetor.isBlank()) {
            setores = setorRepository.findAllWithSalasByNomeContainingIgnoreCase(nomeSetor);
        } else {
            setores = setorRepository.findAllWithSalas();
        }

        // Mapeia a lista de entidades 'Setor' para uma lista de DTOs 'SetorComSalasDto'
        return setores.stream().map(setor -> {

            // Para cada setor, mapeia sua lista de entidades 'Sala' para uma lista de 'SalaParaClienteDto'
            List<SalaParaClienteDto> salasDto = setor.getSalas().stream().map(sala -> {

                // Verifica na lista de agendamentos do cliente se existe algum ativo para esta sala
                Optional<Agendamento> agendamentoAtivo = cliente.getAgendamentos().stream()
                        .filter(ag -> ag.getSala().getId().equals(sala.getId()) &&
                                (ag.getStatus() == StatusAgendamento.SOLICITADO || ag.getStatus() == StatusAgendamento.CONFIRMADO))
                        .findFirst();

                // Extrai o status e o ID do agendamento, se encontrado
                String status = agendamentoAtivo.map(ag -> ag.getStatus().name()).orElse(null);
                Long agendamentoId = agendamentoAtivo.map(Agendamento::getId).orElse(null);

                // Cria o DTO da sala com os dados de contexto do cliente
                return new SalaParaClienteDto(sala, status, agendamentoId);

            }).collect(Collectors.toList());

            // Cria o DTO do setor com a lista de salas já processada
            return new SetorComSalasDto(setor.getId(), setor.getNome(), setor.getDescricao(), salasDto);

        }).collect(Collectors.toList());
    }
}
