package com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NegocioException extends RuntimeException {
    public NegocioException(String message) {
        super(message);
    }
}
