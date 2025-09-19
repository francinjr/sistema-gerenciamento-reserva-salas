package com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SalvarAgendamentoDto {
    @NotNull private Long salaId;

    @NotNull(message = "A data e hora de início são obrigatórias.")
    @Future(message = "A data de início deve ser no futuro.")
    private LocalDateTime dataHoraInicio;

    @NotNull(message = "A data e hora de fim são obrigatórias.")
    @Future(message = "A data de fim deve ser no futuro.")
    private LocalDateTime dataHoraFim;

    @NotNull(message = "A quantidade de pessoas é obrigatória.")
    @Min(value = 1, message = "Deve haver pelo menos 1 pessoa.")
    private Integer quantidadePessoas;
}
