package com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class FiltroRelatorioDto {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataFim;

    private String nomeSetor;
}
