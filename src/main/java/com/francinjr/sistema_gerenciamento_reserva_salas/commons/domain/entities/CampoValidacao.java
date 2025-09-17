package com.francinjr.sistema_gerenciamento_reserva_salas.commons.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CampoValidacao {
    private String name;
    private String message;
}
