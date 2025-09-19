package com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.domain.entities;

public enum StatusAgendamento {
    SOLICITADO,    // Cliente fez o pedido, aguardando confirmação
    CONFIRMADO,    // Recepcionista confirmou
    FINALIZADO,    // Reserva concluída e paga
    CANCELADO      // Reserva foi cancelada pelo cliente ou recusada pela recepcionista
}
