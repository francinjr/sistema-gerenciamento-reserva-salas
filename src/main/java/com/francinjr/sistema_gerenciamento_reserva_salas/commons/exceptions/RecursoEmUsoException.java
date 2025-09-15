package com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RecursoEmUsoException extends RuntimeException {

    public RecursoEmUsoException(String message) {
        super(message);
    }
}
